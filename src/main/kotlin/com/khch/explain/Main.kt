import com.khch.explain.lexer.Lexer
import com.khch.explain.token.TOKEN
import java.util.*

fun main() {
    val sc = Scanner(System.`in`)
    println("Please input something!")

    while (true) {
        val lexer = Lexer()
        lexer.new(sc.nextLine())

        var analyze: TOKEN = lexer.nextToken()

        while (analyze != TOKEN.EOF) {
            println(analyze.name + " -> " + analyze.value)
            analyze = lexer.nextToken()
        }
    }
}