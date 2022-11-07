package com.khch.explain.parse

import com.khch.explain.ast.LetStatement
import com.khch.explain.ast.ReturnStatement
import com.khch.explain.lexer.Lexer
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
}