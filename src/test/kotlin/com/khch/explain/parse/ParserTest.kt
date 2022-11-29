package com.khch.explain.parse

import com.khch.explain.ast.*
import com.khch.explain.lexer.Lexer
import com.khch.explain.token.Token
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails
import kotlin.test.assertTrue

internal class ParserTest {

    private fun checkParseErrors(parser: Parser) {
        if (parser.errors.isEmpty()) {
            return
        }

        for (error in parser.errors) {
            println(error)
        }

        assertFails {
            println("parser has ${parser.errors.size} errors")
        }
    }

    @Test
    fun testLetStatements() {
        val input = """
        let x = 5;
        let y = 10;
        let foobar = 838383;
        """.trimIndent()

        val lexer = Lexer()
        lexer.new(input)

        val parser = Parser()
        parser.new(lexer)

        val program = parser.parseProgram()

        checkParseErrors(parser)

        val size = program.statements.size

        assertEquals(3, size)

        val tests = listOf("x", "y", "foobar")
        tests.forEachIndexed { index, s ->
            run {
                val statement = program.statements[index]
                val letStatement = statement as LetStatement

                val nameTokenLiteral = letStatement.name?.tokenLiteral()
                val nameValue = letStatement.name?.value

                println("nameTokenLiteral $nameTokenLiteral")
                println("nameValue $nameValue")

                assertEquals(s, nameTokenLiteral)
                assertEquals(s, nameValue)
            }
        }
    }

    @Test
    fun testReturnStatement() {
        val input = """
        return 5;
        return 10;
        return 993 322;
        """.trimIndent()

        val lexer = Lexer()
        lexer.new(input)

        val parser = Parser()
        parser.new(lexer)

        val program = parser.parseProgram()
        checkParseErrors(parser)

        val size = program.statements.size

        assertEquals(3, size)

        program.statements.forEach { statement ->
            val returnStatement = statement as ReturnStatement
            assertEquals("return", returnStatement.tokenLiteral())
        }
    }

    @Test
    fun testString() {
        val token = Token()
        token.tokenType = Token.LET
        token.literal = "let"

        val nameToken1 = Token()
        nameToken1.tokenType = Token.IDENT
        nameToken1.literal = "myVar"

        val ident1 = Identifier(token = nameToken1, value = "myVar")

        val nameToken2 = Token()
        nameToken2.tokenType = Token.IDENT
        nameToken2.literal = "anotherVar"

        val ident2 = Identifier(token = nameToken2, value = "anotherVar")

        val letStatement = LetStatement(token)
        letStatement.name = ident1
        letStatement.value = ident2

        println(letStatement.string())

        assertEquals("let myVar = anotherVar;", letStatement.string())
    }

    @Test
    fun testParseExpressions() {
        val input = """
            foobar;
        """.trimIndent()

        val lexer = Lexer()
        lexer.new(input)

        val parser = Parser()
        parser.new(lexer)

        val program = parser.parseProgram();

        checkParseErrors(parser)

        val expressionStatement = program.statements[0] as ExpressionStatement
        val identifier = expressionStatement.expression as Identifier
        assertEquals("foobar", identifier.tokenLiteral())
        assertEquals("foobar", identifier.value)
    }

    @Test
    fun testParseIntExpressions() {
        val input = """
            5;
        """.trimIndent()

        val lexer = Lexer()
        lexer.new(input)

        val parser = Parser()
        parser.new(lexer)

        val program = parser.parseProgram()

        checkParseErrors(parser)

        val expressionStatement = program.statements[0] as ExpressionStatement
        val identifier = expressionStatement.expression as IntegerLiteral
        assertEquals("5", identifier.tokenLiteral())
        assertEquals(5, identifier.value)
    }

    @Test
    fun testParsePrefixExpressions() {
        val prefixTests = arrayOf(
            Triple("!5;", "!", 5),
            Triple("-15;", "-", 15),
        )

        prefixTests.forEach {
            val lexer = Lexer()
            lexer.new(it.first)

            val parser = Parser()
            parser.new(lexer)

            val program = parser.parseProgram()

            checkParseErrors(parser)

            val expressionStatement = program.statements[0] as ExpressionStatement
            val prefixExpression = expressionStatement.expression as PrefixExpression
            assertEquals(it.second, prefixExpression.operator)
            assertTrue(testIntegerLiteral(prefixExpression.right, it.third))
        }
    }

    private fun testIntegerLiteral(expression: Expression?, value: Int): Boolean {
        val integerLiteral = expression as IntegerLiteral? ?: return false

        if (integerLiteral.value != value) {
            return false
        }

        return true
    }

    @Test
    fun testParsingInfixExpressions() {
        data class Infix(val input: String, val leftValue: Int, val operator: String, val rightValue: Int)

        val infixTests = arrayOf(
            Infix("5 + 5;", 5, "+", 5),
            Infix("5 - 5;", 5, "-", 5),
            Infix("5 * 5;", 5, "*", 5),
            Infix("5 / 5;", 5, "/", 5),
            Infix("5 > 5;", 5, ">", 5),
            Infix("5 < 5;", 5, "<", 5),
            Infix("5 == 5;", 5, "==", 5),
            Infix("5 != 5;", 5, "!=", 5),
        )

        infixTests.forEach {
            val lexer = Lexer()
            lexer.new(it.input)

            val parser = Parser()
            parser.new(lexer)

            val program = parser.parseProgram()

            checkParseErrors(parser)

            val expressionStatement = program.statements[0] as ExpressionStatement
            val infixExpression = expressionStatement.expression as InfixExpression

            assertTrue(testIntegerLiteral(infixExpression.left, it.leftValue))
            assertEquals(it.operator, infixExpression.operator)
            assertTrue(testIntegerLiteral(infixExpression.right, it.rightValue))
        }
    }

    @Test
    fun testOperatorPrecedenceParsing() {
        val tests = arrayOf(
            Pair("1 + 2 + 3", "((1 + 2) + 3)"),
            Pair("-a * b", "((-a) * b)"),
            Pair("!-a", "(!(-a))"),
            Pair("a + b + c", "((a + b) + c)"),
            Pair("a + b - c", "((a + b) - c)"),
            Pair("a * b * c", "((a * b) * c)"),
            Pair("a * b / c", "((a * b) / c)"),
            Pair("a + b / c", "(a + (b / c))"),
            Pair("a + b * c + d / e - f", "(((a + (b * c)) + (d / e)) - f)"),
            Pair("3 + 4; -5 * 5", "(3 + 4)((-5) * 5)"),
            Pair("5 > 4 == 3 < 4", "((5 > 4) == (3 < 4))"),
            Pair("5 < 4 != 3 > 4", "((5 < 4) != (3 > 4))"),
            Pair("3 + 4 * 5 == 3 * 1 + 4 * 5", "((3 + (4 * 5)) == ((3 * 1) + (4 * 5)))"),
//            Pair("3 > 5 == false", "((3 > 5) == false)"),
//            Pair("3 < 5 == true", "((3 < 5) == true)"),
        )

        tests.forEach {
            val lexer = Lexer()
            lexer.new(it.first)

            val parser = Parser()
            parser.new(lexer)

            val program = parser.parseProgram()

            checkParseErrors(parser)

            assertEquals(it.second, program.string())
        }
    }

    @Test
    fun testBooleanParsing() {
        val tests = arrayOf(
            Pair("true", true),
            Pair("false", false),
        )

        tests.forEach {
            val lexer = Lexer()
            lexer.new(it.first)

            val parser = Parser()
            parser.new(lexer)

            val program = parser.parseProgram()

            checkParseErrors(parser)

            val expressionStatement = program.statements[0] as ExpressionStatement
            val booleanExpression = expressionStatement.expression as BooleanExpression

            assertEquals(it.second, booleanExpression.value)
        }
    }

    @Test
    fun testBooleanInfixExpressions() {
        data class Infix(val input: String, val leftValue: Boolean, val operator: String, val rightValue: Boolean)

        val infixTests = arrayOf(
            Infix("true == true", true, "==", true),
            Infix("true != false", true, "!=", false),
            Infix("false == false", false, "==", false),
        )

        infixTests.forEach {
            val lexer = Lexer()
            lexer.new(it.input)

            val parser = Parser()
            parser.new(lexer)

            val program = parser.parseProgram()

            checkParseErrors(parser)

            val expressionStatement = program.statements[0] as ExpressionStatement
            val infixExpression = expressionStatement.expression as InfixExpression

            val left = infixExpression.left as BooleanExpression
            val right = infixExpression.right as BooleanExpression

            assertEquals(it.leftValue, left.value)
            assertEquals(it.operator, infixExpression.operator)
            assertEquals(it.rightValue, right.value)
        }
    }

    @Test
    fun testOperatorPrecedenceParsing_1() {

        val tests = arrayOf(
            Pair(
                "1 + (2 + 3) + 4",
                "((1 + (2 + 3)) + 4)"
            ),
            Pair(
                "(5 + 5) * 2",
                "((5 + 5) * 2)"
            ),
            Pair(
                "2 / (5 + 5)",
                "(2 / (5 + 5))"
            ),
            Pair(
                "-(5 + 5)",
                "(-(5 + 5))"
            ),
        )

        tests.forEach {
            val lexer = Lexer()
            lexer.new(it.first)

            val parser = Parser()
            parser.new(lexer)

            val program = parser.parseProgram()

            checkParseErrors(parser)

            assertEquals(it.second, program.string())
        }
    }

    @Test
    fun testIfExpression() {
        val input = """
            if (x < y) { x } else { y }
        """.trimIndent()

        val lexer = Lexer()
        lexer.new(input)

        val parser = Parser()
        parser.new(lexer)

        val parseProgram = parser.parseProgram()
        checkParseErrors(parser)

        val expressionStatement = parseProgram.statements[0] as ExpressionStatement

        val ifExpression = expressionStatement.expression as IfExpression

        val infixExpression = ifExpression.condition as InfixExpression
        assertEquals("x", infixExpression.left?.string())
        assertEquals("<", infixExpression.operator)
        assertEquals("y", infixExpression.right?.string())

        val expressionStatement1 = ifExpression.consequence?.statements?.get(0) as ExpressionStatement
        val expression = expressionStatement1.expression
        assertEquals("x", expression?.string())

        val expressionStatement2 = ifExpression.alternative?.statements?.get(0) as ExpressionStatement
        val expression2 = expressionStatement2.expression
        assertEquals("y", expression2?.string())

        println(parseProgram.string())
    }
}