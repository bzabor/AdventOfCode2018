
class SerialChecker(private val input: List<String>) {

    fun checksumForCountsOf(target1: Int, target2: Int): Int {
        return serialsContainingALetterCountOf(target1) * serialsContainingALetterCountOf(target2)
    }

    fun serialsContainingALetterCountOf(target: Int): Int {
        var serialsCount = 0
        for (line in input) {
            for (char in line) {
                if (line.count { it == char } == target) {
                    serialsCount++
                    break
                }
            }
        }
        return serialsCount
    }


    fun getCommonSerialString(): String {
        lineA@ for ((idxLineA, lineA) in input.withIndex()) {
            lineB@ for (idxLineB in idxLineA+1..input.lastIndex) {

                val lineB = input[idxLineB]
                var diffCount = 0
                var matchingIdx = 0

                charA@ for (idxChar in lineA.indices) {
                        if (lineA[idxChar] != lineB[idxChar]) {
                            matchingIdx = idxChar
                            diffCount++
                            if (diffCount > 1) {
                                continue@lineB
                            }
                        }
                    }

                    if (diffCount == 1) {
                        return lineA.removeRange(matchingIdx..matchingIdx)
                    }
            }
        }
        return ""
    }

}
