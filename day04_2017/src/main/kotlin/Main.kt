import java.io.File

class Main {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val input = File(ClassLoader.getSystemResource("input.txt").file).readLines()
            val checker = PassphraseChecker(input = input)
            println("Part 1 solution: ${checker.getValidCount()}")
            println("Part 2 solution: ${checker.getValidAnagramCount()}")
        }
    }
}
