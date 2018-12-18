
class Day12(private val input: List<String>) {

    val initialState = input[0].split(" ").takeLast(1)[0]

    val rules = input.drop(2).map{ it.split(" => ")}.map{ it[0] to it[1]}.toMap()

    val gens = 500

    var prefixCount = 0

    var initialPrefixLength = 20

    fun part1(): Int {
        println("Initial state: $initialState")
        println("RULES: $rules")

        var newString: String = ".".repeat(20) + initialState + ".".repeat(1000)

        printGen(0, newString)
        for (i in 1..gens) {
            newString = computeNextGen(newString)
            printGen(i, newString)
            printPotSumByGen(i, newString)

        }
        return 0
    }

    private fun printPotSumByGen(gen: Int = gens, currentString: String) {
        println("")

        val negatives = currentString.take(initialPrefixLength + 2 * prefixCount).reversed()
        val positives = currentString.drop(initialPrefixLength + 2 * prefixCount)

        val positivesTotal = positives.mapIndexed { index, c ->  if (c == '#') index else 0}.sum()
        val negativesTotal = negatives.mapIndexed { index, c ->  if (c == '#') index + 1 else 0}.sum()

        println("Total of plant-containing pots after $gen generations is ${positivesTotal - negativesTotal}")
    }

    private fun printGen(gen: Int, genString: String) {
        println("")

        if (gen % 100 == 0) {
            println("GEN: $gen")
        }

        print("GENERATION ${gen % 10} :")
        print("-".repeat(10 - 2*prefixCount))

        for (idx in genString.indices) {

            if (idx < 90) continue
            if (idx > 200) break

            print(genString[idx])
            if (idx == initialPrefixLength - 1) print("|")
        }
    }

    private fun computeNextGen(current: String = initialState): String {
        val prefix =  if (current.take(2).contains("#")) {prefixCount++ ; ".."} else ""
        val suffix =  if (current.takeLast(2).contains("#")) ".." else ""

        var nextString = ""

        for ((idx, pot) in current.withIndex()) {

            val view =  when {

                idx < 2                     -> ".".repeat(2 - idx) + current.slice(0 .. idx + 2)

                idx > current.lastIndex - 2 -> current.slice(idx -2 .. idx + (current.lastIndex - idx)) + ".".repeat(2 - (current.lastIndex - idx))

                else                        -> current.slice(idx -2 .. idx + 2)
            }

            val nextPot = rules.getOrDefault(view, "ZZZZZ")
            nextString += nextPot
        }

        nextString = prefix + nextString + suffix

        return nextString.slice(0..nextString.lastIndex)
    }



    fun part2(): Int {

        println("val at gen 200:   ${valAtGen(200)}")
        println("val at gen 202:   ${valAtGen(202)}")
        println("val at gen 50_000_000_000:   ${valAtGen(50_000_000_000)}")

        return 0
    }


    private fun valAtGen(gen: Long): Long {
        val valAt200Generations = 9120
        return (gen - 200) * 45 + valAt200Generations

    }
}
