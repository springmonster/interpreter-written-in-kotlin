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
            Pair("LET", "let"),
            Pair("IDENT", "add"),
            Pair("EQUALS", "="),
            Pair("FUNCTION", "fn"),
            Pair("LPAREN", "("),
            Pair("IDENT", "x"),
            Pair("COMMA", ","),
            Pair("IDENT", "y"),
            Pair("RPAREN", ")"),
            Pair("LBRACE", "{"),
            Pair("IDENT", "x"),
            Pair("ADD", "+"),
            Pair("IDENT", "y"),
            Pair("SEMICOLON", ";"),
            Pair("RBRACE", "}"),
            Pair("SEMICOLON", ";"),
            Pair("EOF", CONST_EOF.toString()),
        )

//        val input = """
//        let five = 5;
//        let ten = 10;
//        let add = fn(x, y) {
//            x + y;
//        };
//        let result = add(five, ten);
//        !-/*5;
//        5 < 10 > 5;
//        if (5 < 10) {
//            return true;
//        } else {
//            return false;
//        }
//    """.trimIndent()

        val input = """
        let five = 5;
        let ten = 10;
        let add = fn(x, y) { x+y;
        };
    """.trimIndent()

        lexer.new(input)

        expected.forEach {
            val token = lexer.analyze()
            println(token.name + " -> " + token.value)
            assertEquals(it.first, token.name)
            assertEquals(it.second, token.value)
        }
    }
}