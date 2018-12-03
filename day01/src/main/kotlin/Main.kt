import java.io.File

class Main {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {

            val input = File(ClassLoader.getSystemResource("input.txt").file).readLines()

            val calculator = FrequencyOffset(input = input)

            println("Part 1 solution: ${calculator.calculateOffset()}")
            println("Part 2 solution: ${calculator.findFirstRepeatFrequency()}")
        }
    }
}
