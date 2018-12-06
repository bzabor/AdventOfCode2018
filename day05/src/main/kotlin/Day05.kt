class Day05(private val input: List<String>) {

    fun part1(): Int {
        return reducePolymer(input[0]).length
    }

    fun part2(): Int {
        var minLetterLength = Int.MAX_VALUE
        for (letter in 'a'..'z') {
            val stripped = input[0]
                    .replace(letter.toString().toLowerCase(), "")
                    .replace(letter.toString().toUpperCase(), "")
            if (reducePolymer(stripped).length < minLetterLength) {
                minLetterLength = reducePolymer(stripped).length
            }
        }
        return minLetterLength
    }

    private fun reducePolymer(toReduce: String): String {
        var s = ""
        for (char in toReduce) {
            if (s.isNotEmpty()
                    && s.last().equals(char, ignoreCase = true)
                    && !s.last().equals(char, ignoreCase = false)) {
                s = s.dropLast(1)
                continue
            } else {
                s += char
            }
        }
        return s
    }
}
