import java.io.File

class Main {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {

            val input = File(ClassLoader.getSystemResource("input.txt").file)
            val contents = input.readText(Charsets.UTF_8).trimEnd()

            println("CONTENTS ARE: ${contents}@@@")

            val calculator = CaptchaCalculator(input = contents.toString())

            println("Part 1 solution: ${calculator.getSolutionAdjacentPairs()}")
//        println("Part 2 solution: ${calculator.getSolutionOppositePairs()}")
        }
    }
}
