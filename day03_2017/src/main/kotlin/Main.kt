import java.io.File

fun main() {
    val input = File(ClassLoader.getSystemResource("input.txt").file)

    val x = 450.00

    val y = Math.sqrt(x)

    println("$x sqrt --> $y")


}
