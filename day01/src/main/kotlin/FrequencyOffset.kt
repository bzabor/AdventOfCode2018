class FrequencyOffset(private val input: List<String>) {

    fun calculateOffset(): Int {
        return input.sumBy { Integer.parseInt(it) }
    }

    fun findFirstRepeatFrequency(): Int {
        val freqSet = mutableSetOf<Int>()
        var currentFrequency = 0
        while (true) {
            for (line in input) {
                currentFrequency += Integer.parseInt(line)
                if (freqSet.contains(currentFrequency)) {
                    return currentFrequency
                }
                else {
                    freqSet.add(currentFrequency)
                }
            }
        }
    }
}
