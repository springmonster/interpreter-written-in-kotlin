import com.khch.explain.lexer.Lexer
import com.khch.explain.token.TOKEN

fun main() {
    val input = """
        let five = 5;
        let ten = 10;
    """.trimIndent()

    val lexer = Lexer()

    lexer.new(input)

    var token = lexer.analyze()
    println(token.name + " -> " + token.value)

    while (token != TOKEN.EOF) {
        token = lexer.analyze()
        println(token.name + " -> " + token.value)
    }
}