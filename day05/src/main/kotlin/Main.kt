import java.io.File

class Main {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {

            val input = File(ClassLoader.getSystemResource("input.txt").file).readLines()

            val day05 = Day05(input = input)

            println("Part 1 solution: ${day05.part1()}")
            println("Part 2 solution: ${day05.part2()}")
        }
    }
}
