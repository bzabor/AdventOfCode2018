class Day10(input: List<String>) {

    val regex = Regex("""[^-0-9]+""")
    val points = input.map { it ->
        val (a, b, c, d) = it.split(regex).drop(1).dropLast(1).map { it.toInt() }
        Point(a, b, c, d)
    }

    fun part1(): Int {

        for (seconds in 0..15000) {
            val minMax = getGridDimensionsForPoints()
            val (minX, minY, maxX, maxY) = minMax
            if (maxX - minX < 100) {
                drawPoints(minX, minY, maxX, maxY)
            }
            incrementPoints()
        }
        return 0
    }

    fun incrementPoints() {
        points.forEach { it -> it.x += it.velocityX; it.y += it.velocityY }
    }

    fun drawPoints(minX: Int, minY: Int, maxX: Int, maxY: Int) {
        for (y in minY..maxY) {
            val xPoints = points.filter { it.y == y }.map { it.x }
            if (xPoints.size < 10) continue
            for (x in minX..maxX) {
                if (xPoints.contains(x)) print("# ") else print(". ")
            }
            println("")
        }
        println("")
        println("")
    }

    fun getGridDimensionsForPoints(): List<Int> {
        val minX = points.minBy { it.x }!!.x
        val minY = points.minBy { it.y }!!.y
        val maxX = points.maxBy { it.x }!!.x
        val maxY = points.maxBy { it.y }!!.y
        return listOf(minX, minY, maxX, maxY)
    }

    fun part2(): Int {
        return 0
    }
}


class Point(var x: Int, var y: Int, val velocityX: Int, val velocityY: Int) {
    override fun toString(): String {
        return "x: $x,  y: $y,  velocityX: $velocityX,  velocityY: $velocityY"
    }
}
