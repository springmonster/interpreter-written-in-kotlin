package com.khch.explain.lexer

import com.khch.explain.token.CONST_EOF
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class LexerTest {

    private val lexer = Lexer()

    @Test
    fun analyze() {
        val expected = listOf(
            Pair("LET", "let"),
            Pair("IDENT", "five"),
            Pair("EQUALS", "="),
            Pair("IDENT", "5"),
            Pair("SEMICOLON", ";"),
            Pair("LET", "let"),
            Pair("IDENT", "ten"),
            Pair("EQUALS", "="),
            Pair("IDENT", "10"),
            Pair("SEMICOLON", ";"),
            Pair("EOF", CONST_EOF.toString()),
        )

        val input = """
        let five = 5;
        let ten = 10;
    """.trimIndent()

        lexer.new(input)

        expected.forEach {
            val token = lexer.analyze()
            assertEquals(it.first, token.name)
            assertEquals(it.second, token.value)
        }
    }
}