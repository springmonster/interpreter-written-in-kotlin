package com.khch.explain.parse

import com.khch.explain.lexer.Lexer
import org.junit.jupiter.api.Test

internal class ParserTest {
    @Test
    fun parse() {
        val input = """
        let x = 5;
        let y = 10;
        let foobar = 838383;
        """.trimIndent()

        val lexer = Lexer()
        lexer.new(input)

        val parser = Parser()
        parser.new(lexer)

        val parseProgram = parser.parseProgram()
        println(parseProgram)
    }
}