import kotlin.math.absoluteValue

class Day06(input: List<String>) {

    private val landingPlaces = input.map { it ->
        val (row, col) = it.split(", ").map { it.toInt() }
        LandingPlace(row, col)
    }

    fun part1(): Int {
        val gridSize = 1000
        for (row in 0 until gridSize) {
            for (col in 0 until gridSize) {
                val nearestLandingPlace = nearestLandingPlace(row, col)
                if (nearestLandingPlace != null) {
                    nearestLandingPlace.nearestList.add(Pair(row, col))
                    if (row == 0 || col == 0 || row == gridSize - 1 || col == gridSize - 1) {
                        nearestLandingPlace.isInfinite = true
                    }
                }
            }
        }

        var largestLandingPlace: LandingPlace? = null
        var maxLandingPlaceSize = 0
        for (landingPlace in landingPlaces.filterNot { it.isInfinite }) {
            if (landingPlace.nearestList.size > maxLandingPlaceSize) {
                maxLandingPlaceSize = landingPlace.nearestList.size
                largestLandingPlace = landingPlace
            }
        }
        println("largest landing place is ${largestLandingPlace?.id} with size of $maxLandingPlaceSize")
        return maxLandingPlaceSize
    }

    private fun nearestLandingPlace(row: Int, col: Int): LandingPlace? {
        val nearestPlaces = mutableListOf<LandingPlace>()
        var minDistance = Int.MAX_VALUE
        for (landingPlace in landingPlaces) {
            val distance = pointToLandingPlaceDistance(row, col, landingPlace)
            if (distance < minDistance) {
                minDistance = distance
                nearestPlaces.clear()
                nearestPlaces.add(landingPlace)
            } else if (distance == minDistance) {
                nearestPlaces.add(landingPlace)
            }
        }
        return if (nearestPlaces.size == 1) {
            nearestPlaces[0]
        } else {
            null
        }
    }

    private fun pointToLandingPlaceDistance(row: Int, col: Int, landingPlace: LandingPlace): Int {
        return (landingPlace.row - row).absoluteValue + (landingPlace.col - col).absoluteValue
    }

    fun part2(): Int {
        val gridRange = 10001
        var locationWithTotalDistanceLessThan10000Count = 0
        for (row in -gridRange..gridRange) {
            for (col in -gridRange..gridRange) {
                var distance = 0
                for (landingPlace in landingPlaces) {
                    distance += pointToLandingPlaceDistance(row, col, landingPlace)
                }
                if (distance < 10000) {
                    locationWithTotalDistanceLessThan10000Count++
                }
            }
        }
        return locationWithTotalDistanceLessThan10000Count
    }
}

class LandingPlace(val row: Int, val col: Int) {
    val id: String = "$row:$col"
    var isInfinite = false
    val nearestList = mutableListOf<Pair<Int,Int>>()
}

