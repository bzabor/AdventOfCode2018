class Day14(private val input: List<String>) {

    val recipeMin = 607331
    val recipies = mutableListOf(3, 7)

    var elf1Index = 0
    var elf2Index = 1
    var tryCount = 1

    fun part1(): Int {

        while (recipies.size <= recipeMin + 10) {
            tryCount++
            recipies.addAll(nextRecipes())
            elf1Index = updateElfIndex(elf1Index)
            elf2Index = updateElfIndex(elf2Index)
        }

        val result = recipies.slice(recipeMin..recipeMin + 9)
        println("")
        println(result)
        println("")
        print("After $recipeMin recipies: ")
        result.forEach { print(it) }
        println("")

        return 0
    }

    //    To create new recipes, the two Elves combine their current recipes. This creates new recipes from the
    //    digits of the sum of the current recipes' scores. With the current recipes' scores of 3 and 7, their
    //    sum is 10, and so two new recipes would be created: the first with score 1 and the second with score 0.
    //    If the current recipes' scores were 2 and 3, the sum, 5, would only create one recipe
    //    (with a score of 5) with its single digit.
    private fun nextRecipes(): List<Int> {
        return (recipies[elf1Index] + recipies[elf2Index]).toString().toList().map { Integer.parseInt(it.toString()) }
    }

    //    To do this, the Elf steps forward through the scoreboard a number of recipes equal to 1 plus the score
    //    of their current recipe. So, after the first round, the first Elf moves forward 1 + 3 = 4 times, while
    //    the second Elf moves forward 1 + 7 = 8 times. If they run out of recipes, they loop back around to the
    //    beginning.
    private fun updateElfIndex(elfIndex: Int): Int {
        val recipeVal = recipies[elfIndex]
        var newIndex = elfIndex + recipeVal + 1
        if (newIndex > recipies.lastIndex) {
            newIndex = newIndex % recipies.size
        }
        return newIndex
    }

    private fun printRecipies() {
        println("")
        recipies.withIndex().forEach { (idx, int) ->
            print(
                    when (idx) {
                        elf1Index -> "(${int})"
                        elf2Index -> "[${int}]"
                        else -> " $int "
                    })
        }
    }

    val testString = "607331"
    var baseRemove = 607000

    fun part2(): Int {
        while (getFirstIndexOf(testString) < 0 && tryCount < 30000000) {
            tryCount++
            if (tryCount % 1000 == 0) println("tryCount: $tryCount recipe length: ${recipies.size} + baseRemove: $baseRemove")

            val nextRecipies = nextRecipes()
            baseRemove += nextRecipies.size
            recipies.addAll(nextRecipies)
            elf1Index = updateElfIndex(elf1Index)
            elf2Index = updateElfIndex(elf2Index)
        }
        println("First index of $testString is ${getFirstIndexOf(testString) + baseRemove}")
        return 0
    }

    private fun getFirstIndexOf(s: String): Int {
        val recipeString = recipies.drop(baseRemove).joinToString(separator = "")
        return recipeString.indexOf(testString)
    }
}
