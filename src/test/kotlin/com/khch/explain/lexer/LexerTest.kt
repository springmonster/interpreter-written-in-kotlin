package com.khch.explain.lexer

import com.khch.explain.token.CONST_EOF
import com.khch.explain.token.Token
import com.khch.explain.token.TokenType
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class LexerTest {

    private val lexer = Lexer()

    @Test
    fun analyze() {
        val input = """
        let five = 5;
        let ten = 10;
        let add = fn(x, y) { x+y;};
        let result = add(five, ten);
        !-/*5;
        5<10>5;
        if (5 < 10) {return true;
        } else {return false;
        }
        10 == 10;
        10 != 9;
        """.trimIndent()

        val expected: List<Pair<TokenType, String>> = listOf(
            Pair(Token.LET, "let"),
            Pair(Token.IDENT, "five"),
            Pair(Token.ASSIGN, "="),
            Pair(Token.INT, "5"),
            Pair(Token.SEMICOLON, ";"),
            Pair(Token.LET, "let"),
            Pair(Token.IDENT, "ten"),
            Pair(Token.ASSIGN, "="),
            Pair(Token.INT, "10"),
            Pair(Token.SEMICOLON, ";"),
            Pair(Token.LET, "let"),
            Pair(Token.IDENT, "add"),
            Pair(Token.ASSIGN, "="),
            Pair(Token.FUNCTION, "fn"),
            Pair(Token.LPAREN, "("),
            Pair(Token.IDENT, "x"),
            Pair(Token.COMMA, ","),
            Pair(Token.IDENT, "y"),
            Pair(Token.RPAREN, ")"),
            Pair(Token.LBRACE, "{"),
            Pair(Token.IDENT, "x"),
            Pair(Token.PLUS, "+"),
            Pair(Token.IDENT, "y"),
            Pair(Token.SEMICOLON, ";"),
            Pair(Token.RBRACE, "}"),
            Pair(Token.SEMICOLON, ";"),
            Pair(Token.LET, "let"),
            Pair(Token.IDENT, "result"),
            Pair(Token.ASSIGN, "="),
            Pair(Token.IDENT, "add"),
            Pair(Token.LPAREN, "("),
            Pair(Token.IDENT, "five"),
            Pair(Token.COMMA, ","),
            Pair(Token.IDENT, "ten"),
            Pair(Token.RPAREN, ")"),
            Pair(Token.SEMICOLON, ";"),
            Pair(Token.BANG, "!"),
            Pair(Token.MINUS, "-"),
            Pair(Token.SLASH, "/"),
            Pair(Token.ASTERISK, "*"),
            Pair(Token.INT, "5"),
            Pair(Token.SEMICOLON, ";"),
            Pair(Token.INT, "5"),
            Pair(Token.LT, "<"),
            Pair(Token.INT, "10"),
            Pair(Token.GT, ">"),
            Pair(Token.INT, "5"),
            Pair(Token.SEMICOLON, ";"),
            Pair(Token.IF, "if"),
            Pair(Token.LPAREN, "("),
            Pair(Token.INT, "5"),
            Pair(Token.LT, "<"),
            Pair(Token.INT, "10"),
            Pair(Token.RPAREN, ")"),
            Pair(Token.LBRACE, "{"),
            Pair(Token.RETURN, "return"),
            Pair(Token.TRUE, "true"),
            Pair(Token.SEMICOLON, ";"),
            Pair(Token.RBRACE, "}"),
            Pair(Token.ELSE, "else"),
            Pair(Token.LBRACE, "{"),
            Pair(Token.RETURN, "return"),
            Pair(Token.FALSE, "false"),
            Pair(Token.SEMICOLON, ";"),
            Pair(Token.RBRACE, "}"),
            Pair(Token.INT, "10"),
            Pair(Token.EQ, "=="),
            Pair(Token.INT, "10"),
            Pair(Token.SEMICOLON, ";"),
            Pair(Token.INT, "10"),
            Pair(Token.NOT_EQ, "!="),
            Pair(Token.INT, "9"),
            Pair(Token.SEMICOLON, ";"),
            Pair(Token.EOF, CONST_EOF.toString()),
        )

        lexer.new(input)

        expected.forEach {
            val token = lexer.nextToken()
            println(token.tokenType + " -> " + token.literal)
            assertEquals(it.first, token.tokenType)
            assertEquals(it.second, token.literal)
        }
    }
}