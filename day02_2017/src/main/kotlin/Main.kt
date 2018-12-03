import java.io.File

class Main {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {

            val input = File(ClassLoader.getSystemResource("input.txt").file).readLines()

            val calculator = ChecksumCalculator(input = input)

            println("Part 1 solution: ${calculator.getChecksum()}")
        }
    }
}
