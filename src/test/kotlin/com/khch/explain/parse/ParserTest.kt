package com.khch.explain.parse

import com.khch.explain.ast.Ast
import com.khch.explain.ast.Identifier
import com.khch.explain.ast.LetStatement
import com.khch.explain.ast.ReturnStatement
import com.khch.explain.ast.interfaces.Expression
import com.khch.explain.lexer.Lexer
import com.khch.explain.token.Token
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class ParserTest {
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

        val ast = parser.parseProgram()
        val size = ast.statements.size

        assertEquals(3, size)

        val tests = listOf("x", "y", "foobar")
        tests.forEachIndexed { index, s ->
            run {
                val statement = ast.statements[index]
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

        val ast = parser.parseProgram()
        val size = ast.statements.size

        assertEquals(3, size)

        ast.statements.forEach { statement ->
            val returnStatement = statement as ReturnStatement
            assertEquals("return", returnStatement.tokenLiteral())
        }
    }

    @Test
    fun testString() {
        val ast = Ast()

        val token = Token()
        token.tokenType = Token.LET
        token.literal = "let"

        val nameToken1 = Token()
        nameToken1.tokenType = Token.IDENT
        nameToken1.literal = "myVar"

        val ident1 = Identifier("myVar", nameToken1)

        val nameToken2 = Token()
        nameToken2.tokenType = Token.IDENT
        nameToken2.literal = "anotherVar"

        val ident2 = Identifier("anotherVar", nameToken2)

        val letStatement = LetStatement(token)
        letStatement.name = ident1
        letStatement.value = ident2

        println(letStatement.string())
    }
}