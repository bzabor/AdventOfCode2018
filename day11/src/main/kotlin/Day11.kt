class Day11(private val input: List<String>?) {

    val gridSize = 301
    val cellGrid = Array(gridSize) { Array(gridSize) { 0 } }

    fun part1(): String {
        setPowerLevel(7989)
        return findMaxPowerForDim(3).toString()
    }

    fun part2(): String {
        return findMaxDim().toString()
    }


    //Find the fuel cell's rack ID, which is its X coordinate plus 10.
    //Begin with a power level of the rack ID times the Y coordinate.
    //Increase the power level by the value of the grid serial number (your puzzle input).
    //Set the power level to itself multiplied by the rack ID.
    //Keep only the hundreds digit of the power level (so 12345 becomes 3; numbers with no hundreds digit become 0).
    //Subtract 5 from the power level.

    fun setPowerLevel(gridSerialNumber: Int) {
        for (y in 1 until gridSize) {
            for (x in 1 until gridSize) {
                val rackId = x + 10
                val powerLevel = (rackId * (rackId * y + gridSerialNumber)).toString()
                var hundredsDigit = if (powerLevel.length >= 3) powerLevel.dropLast(2).takeLast(1).toInt() else 0
                hundredsDigit -= 5
                cellGrid[x][y] = hundredsDigit
            }
        }
    }

    private fun findMaxPowerForDim(gridDim: Int): Int {
        var maxPowerX = 0
        var maxPowerY = 0
        var maxPower = 0

        for (y in 1 until gridSize - gridDim) {
            for (x in 1 until gridSize - gridDim) {

                var power = 0

                for (powerY in y until y + gridDim) {
                    for (powerX in x until x + gridDim) {

                        power += cellGrid[powerX][powerY]
                        if (power > maxPower) {
                            maxPowerX = x
                            maxPowerY = y
                            maxPower = power
                        }
                    }
                }
            }
        }

        println("MAX FOR DIM $gridDim x $gridDim AT LOCATION (${maxPowerX},${maxPowerY}) IS $maxPower")
        return maxPower
    }

    private fun findMaxDim(): Int {

        var maxPowerDim = 0
        var maxPower = 0

        for (gridDim in 1 until gridSize) {
//        for (gridDim in 16..16) {
            val maxPowerForDim = findMaxPowerForDim(gridDim)
            if (maxPowerForDim > maxPower) {
                maxPower = maxPowerForDim
                maxPowerDim = gridDim
                println("Max power grid dim: $maxPowerDim x $maxPowerDim  has a MAX POWER of $maxPower")
            }
        }

        println("FINAL MAX POWER DIM: $maxPowerDim x $maxPowerDim with MAX POWER OF $maxPower")
        return maxPowerDim
    }


}


//
