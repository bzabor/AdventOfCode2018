class CaptchaCalculator(private var input: String) {

    fun getSolutionAdjacentPairs(): Int {
        var sum = 0
        input += input.first()
        for (i in 0 until input.lastIndex) {
            if (input[i] == input[i + 1]) {
                sum += Integer.parseInt("${input[i]}")
            }
        }
        return sum
    }
}
