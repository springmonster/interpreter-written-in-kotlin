package com.khch.explain.evaluator

import com.khch.explain.lexer.Lexer
import com.khch.explain.`object`.*
import com.khch.explain.parse.Parser
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails
import kotlin.test.assertTrue

internal class EvaluatorTest {

    private fun testEval(input: String): Object? {
        val lexer = Lexer()
        lexer.new(input)

        val parser = Parser()
        parser.new(lexer)

        val program = parser.parseProgram()

        return eval(program, Environment().newEnvironment())
    }

    private fun testIntegerObject(expected: Int, obj: Object?): Boolean {
        val actual = obj as IntegerObj
        return expected == actual.value
    }

    private fun testBooleanObject(expected: Boolean, obj: Object?): Boolean {
        val actual = obj as BooleanObj
        return expected == actual.value
    }

    private fun testNullObject(obj: Object?): Boolean {
        return obj == null
    }

    @Test
    fun testEvalIntegerExpression() {
        val tests = arrayOf(
            Pair("5", 5),
            Pair("10", 10),
            Pair("-10", 10),
            Pair("-5", 5),
            Pair("5 + 5 + 5 + 5 - 10", 10),
            Pair("2 * 2 * 2 * 2 * 2", 32),
            Pair("-50 + 100 + -50", 0),
            Pair("5 * 2 + 10", 20),
            Pair("5 + 2 * 10", 25),
            Pair("20 + 2 * -10", 0),
            Pair("50 / 2 * 2 + 10", 60),
            Pair("2 * (5 + 10)", 30),
            Pair("3 * 3 * 3 + 10", 37),
            Pair("3 * (3 * 3) + 10", 37),
            Pair("(5 + 10 * 2 + 15 / 3) * 2 + -10", 50),
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
            Pair("1 < 2", true),
            Pair("1 > 2", false),
            Pair("1 < 1", false),
            Pair("1 > 1", false),
            Pair("1 == 1", true),
            Pair("1 != 1", false),
            Pair("1 == 2", false),
            Pair("1 != 2", true),
            Pair("true == true", true),
            Pair("false == false", true),
            Pair("true == false", false),
            Pair("true == false", false),
            Pair("false != true", true),
            Pair("(1 < 2) == true", true),
            Pair("(1 < 2) == false", false),
            Pair("(1 > 2) == true", false),
            Pair("(1 > 2) == false", true),
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

    @Test
    fun testIfExpressions() {
        val tests = arrayOf(
            Pair("if (true) {10}", 10),
            Pair("if (false) {10}", null),
            Pair("if (1) {10}", 10),
            Pair("if (1<2) {10}", 10),
            Pair("if (1>2) {10}", null),
            Pair("if (1>2) {10}", null),
            Pair("if (1>2) {10} else {20}", 20),
            Pair("if (1<2) {10} else {20}", 10),
        )

        tests.forEach {
            val testEval = testEval(it.first)
            if (it.second is Int) {
                testIntegerObject(it.second as Int, testEval)
            } else {
                testNullObject(testEval)
            }
        }
    }

    @Test
    fun testReturnStatements() {
        val tests = arrayOf(
            Pair("return 10;", 10),
            Pair("return 10;9;", 10),
            Pair("return 2*5;9;", 10),
            Pair("9; return 2*5;9", 10),
            Pair(
                """
                if (10 > 1) {
                    if (10 > 1) {
                        return 10;
                    }
                    return 1;
                }
            """.trimIndent(), 10
            )
        )

        tests.forEach {
            val testEval = testEval(it.first)
            testIntegerObject(it.second, testEval)
        }
    }

    @Test
    fun testErrorHandling() {
        val tests = arrayOf(
            Pair("5 + true", "type mismatch: INTEGER + BOOLEAN"),
            Pair("5 + true;5;", "type mismatch: INTEGER + BOOLEAN"),
            Pair("-true", "unknown operator: -BOOLEAN"),
            Pair("true + false", "unknown operator: BOOLEAN + BOOLEAN"),
            Pair("5; true + false; 5", "unknown operator: BOOLEAN + BOOLEAN"),
            Pair("if (10 > 1) { true + false; }", "unknown operator: BOOLEAN + BOOLEAN"),
            Pair(
                """
                if (10 > 1) {
                    if (10 > 1) {
                        return true + false;
                    }
                    return 1;
                }
            """, "unknown operator: BOOLEAN + BOOLEAN"
            ),
            Pair("foobar", "identifier not found: foobar"),
        )

        tests.forEach {
            val testEval = testEval(it.first)
            if (testEval is ErrorObj) {
                assertEquals(it.second, testEval.message)
            } else {
                assertFails { "no errors??? $testEval" }
            }
        }
    }

    @Test
    fun testLetStatements() {
        val tests = arrayOf(
            Pair("let a = 5;a;", 5),
            Pair("let a = 5 * 5;a;", 25),
            Pair("let a = 5; let b = a; b;", 5),
            Pair("let a = 5; let b = a; let c = a + b + 5;c;", 15),
        )

        tests.forEach {
            val testEval = testEval(it.first)
            testIntegerObject(it.second, testEval)
        }
    }

    @Test
    fun testFunctionObject() {
        val input = """
            fn(x) { x + 2; };
        """.trimIndent()

        val eval = testEval(input)

        assertTrue(eval is FunctionObj)

        val function = eval as FunctionObj

        if (function.parameters.size != 1) {
            assertFails {
                "wrong number of parameters: ${function.parameters.size}"
            }
        }

        if (function.parameters[0].string() != "x") {
            assertFails {
                "parameter is not 'x'"
            }
        }

        val expectedBody = "(x + 2)"

        assertEquals(expectedBody, function.body.string())

        println(eval.inspect())
    }

    @Test
    fun testFunctionApplication() {
        val tests = arrayOf(
            Pair("let identity = fn(x) { x; }; identity(5);", 5),
            Pair("let identity = fn(x) { return x; }; identity(5);", 5),
            Pair("let double = fn(x) { x * 2; }; double(5);", 10),
            Pair("let add = fn(x, y) { x + y; }; add(5, 5);", 10),
            Pair("let add = fn(x, y) { x + y; }; add(5 + 5, add(5, 5));", 20),
            Pair("fn(x) { x; }(5)", 5),
        )

        tests.forEach {
            val testEval = testEval(it.first)
            testIntegerObject(it.second, testEval)
        }
    }
}