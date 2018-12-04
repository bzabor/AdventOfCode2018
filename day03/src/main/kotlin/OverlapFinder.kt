class OverlapFinder(private val input: List<String>) {


    val clothDim = 1000
    val regex = Regex("""\D+""")
    val spots = Array(clothDim) { Array(clothDim) {0} }

    fun findOverlapCount(): Int {
        for (line in input) {
            val (claimNumber, left, top, width, height) = regex.split(line.drop(1)).map { it.toInt() }
            applyClaim(left, top, width, height)
        }
        return getCountOfTwoOrMoreClaims()
    }

    fun getPureClaimId():Int {
        for (line in input) {
            val (claimNumber, left, top, width, height) = regex.split(line.drop(1)).map { it.toInt() }
            if (isPureClaim(left, top, width, height)) return claimNumber
        }
        return 0
    }

    private fun applyClaim(left: Int, top: Int, width: Int, height: Int) {
        for (row in top until top+height) {
            for (col in left until left+width) {
                spots[row][col]++
            }
        }
    }

    private fun getCountOfTwoOrMoreClaims(): Int {
        var count = 0
        for (row in spots) {
            for (item in row) {
                if (item > 1) count++
            }
        }
        return count
    }

    private fun isPureClaim(left: Int, top: Int, width: Int, height: Int): Boolean {
         for (row in top until top+height) {
            for (col in left until left+width) {
                if(spots[row][col] != 1) return false
            }
        }
        return true
    }
}
