package com.khch.explain.evaluator

import com.khch.explain.lexer.Lexer
import com.khch.explain.`object`.BooleanObj
import com.khch.explain.`object`.IntegerObj
import com.khch.explain.`object`.Object
import com.khch.explain.parse.Parser
import org.junit.jupiter.api.Test

internal class EvaluatorTest {

    private fun testEval(input: String): Object? {
        val lexer = Lexer()
        lexer.new(input)

        val parser = Parser()
        parser.new(lexer)

        val program = parser.parseProgram()

        return eval(program)
    }

    private fun testIntegerObject(expected: Int, obj: Object?): Boolean {
        val actual = obj as IntegerObj
        return expected == actual.value
    }

    private fun testBooleanObject(expected: Boolean, obj: Object?): Boolean {
        val actual = obj as BooleanObj
        return expected == actual.value
    }

    @Test
    fun testEvalIntegerExpression() {
        val tests = arrayOf(
            Pair("5", 5),
            Pair("10", 10),
            Pair("-10", 10),
            Pair("-5", 5),
        )

        tests.forEach {
            val testEval = testEval(it.first)
            testIntegerObject(it.second, testEval)
        }
    }

    @Test
    fun testEvalBooleanExpression() {
        val tests = arrayOf(
            Pair("true", true),
            Pair("false", false),
        )

        tests.forEach {
            val testEval = testEval(it.first)
            testBooleanObject(it.second, testEval)
        }
    }

    @Test
    fun testBangOperator() {
        val tests = arrayOf(
            Pair("!true", false),
            Pair("!false", true),
            Pair("!5", false),
            Pair("!!true", true),
            Pair("!!false", false),
            Pair("!!5", true),
        )

        tests.forEach {
            val testEval = testEval(it.first)
            testBooleanObject(it.second, testEval)
        }
    }
}