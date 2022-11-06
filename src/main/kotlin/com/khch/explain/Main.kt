import com.khch.explain.lexer.Lexer
import com.khch.explain.token.Token
import java.util.*

fun main() {
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