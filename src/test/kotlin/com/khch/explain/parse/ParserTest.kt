package com.khch.explain.parse

import com.khch.explain.ast.ExpressionStatement
import com.khch.explain.ast.Identifier
import com.khch.explain.ast.LetStatement
import com.khch.explain.ast.ReturnStatement
import com.khch.explain.lexer.Lexer
import com.khch.explain.token.Token
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails

internal class ParserTest {

    private fun checkParseErrors(parser: Parser) {
        if (parser.errors.isEmpty()) {
            return
        }

        for (error in parser.errors) {
            println(error)
        }

        assertFails {
            println("parser hs ${parser.errors.size} errors")
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

        val program = parser.parseProgram();

        val expressionStatement = program.statements[0] as ExpressionStatement
        val identifier = expressionStatement.expression as Identifier
        assertEquals("5", identifier.tokenLiteral())
        assertEquals("5", identifier.value)
    }
}