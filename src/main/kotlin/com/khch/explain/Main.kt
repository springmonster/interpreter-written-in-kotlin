import com.khch.explain.evaluator.eval
import com.khch.explain.lexer.Lexer
import com.khch.explain.parse.Parser
import com.khch.explain.token.Token
import java.util.*

object MONKEY {
    const val MONKEY_FACE = """
            __,__
   .--.  .-"     "-.  .--.
  / .. \/  .-. .-.  \/ .. \
 | |  '|  /   Y   \  |'  | |
 | \   \  \ 0 | 0 /  /   / |
  \ '- ,\.-'''''''-./, -' /
   ''-' /_   ^ ^   _\ '-''
       |  \._   _./  |
       \   \ '~' /   /
        '._ '-=-' _.'
           '-----'
    """
}


fun main() {
//    lexerRepl()

//    parserRepl()

    evaluatorRepl()
}

private fun evaluatorRepl() {
    val sc = Scanner(System.`in`)
    println("Please input something!")

    while (true) {
        val lexer = Lexer()
        lexer.new(sc.nextLine())

        val parser = Parser()
        parser.new(lexer)

        val program = parser.parseProgram()

        if (parser.errors.isNotEmpty()) {
            println(MONKEY.MONKEY_FACE)
            println("Woops! We ran into some monkey business here!")
            println(parser.errors.joinToString("\n"))
            continue
        }

        val evaluated = eval(program)
        println(evaluated?.inspect())
    }
}

/**
 * 正确的用例
 * let x = 1 * 2 * 3 * 4 * 5
 * x * y / 2 + 3 * 8 - 123
 * true == false
 *
 * 错误的用例
 * let x 12 * 3;
 */
private fun parserRepl() {
    val sc = Scanner(System.`in`)
    println("Please input something!")

    while (true) {
        val lexer = Lexer()
        lexer.new(sc.nextLine())

        val parser = Parser()
        parser.new(lexer)

        val program = parser.parseProgram()

        if (parser.errors.isNotEmpty()) {
            println(MONKEY.MONKEY_FACE)
            println("Woops! We ran into some monkey business here!")
            println(parser.errors.joinToString("\n"))
            continue
        }

        println(program.string())
    }
}

private fun lexerRepl() {
    val sc = Scanner(System.`in`)
    println("Please input something!")

    while (true) {
        val lexer = Lexer()
        lexer.new(sc.nextLine())

        var analyze: Token = lexer.nextToken()

        while (analyze.tokenType != Token.EOF) {
            println(analyze.tokenType + " -> " + analyze.literal)
            analyze = lexer.nextToken()
        }
    }
}