import java.io.File

class Main {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {

            val input = File(ClassLoader.getSystemResource("input.txt").file).readLines()

            val calculator = OverlapFinder(input = input)

            println("Part 1 solution: ${calculator.findOverlapCount()}")
            println("Part 2 solution: ${calculator.getPureClaimId()}")
        }
    }
}
