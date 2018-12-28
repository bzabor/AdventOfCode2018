import kotlin.math.abs

class Day25(private val input: List<String>) {


    val points = mutableListOf<Point>()
    val constellations = mutableListOf(mutableListOf<Point>())

    private fun init() {
        var idx = 0
        for (line in input) {
            val a = line.split("""[^-0-9]+""".toRegex()).drop(1).map { it.toInt() }
            points.add(Point(a[0], a[1], a[2], a[3]))
        }
    }


    fun part1(): Int {

        init()

        for (point in points) {
            val matchingConstellations = mutableListOf(mutableListOf<Point>())
            for (constellation in constellations) {
                for (constellationPoint in constellation) {
                    if (manahttanDistance(point, constellationPoint) <= 3) {
                        matchingConstellations.add(constellation)
                        break
                    }
                }
            }

            if (matchingConstellations.isEmpty()) {
                constellations.add(mutableListOf(point))
            } else {
                constellations.removeAll(matchingConstellations)
                matchingConstellations.add(mutableListOf(point))
                constellations.add(matchingConstellations.flatten().toMutableList())
            }
        }

        return constellations.size
    }

    private fun manahttanDistance(pointA: Point, pointB: Point) =
            abs(pointA.w - pointB.w) +
                    abs(pointA.x - pointB.x) +
                    abs(pointA.y - pointB.y) +
                    abs(pointA.z - pointB.z)

    fun part2(): Int {
        return 0
    }
}


class Point(
        val w: Int = 0,
        val x: Int = 0,
        val y: Int = 0,
        val z: Int = 0
) {
    override fun toString() = "$w:$x:$y:$z"
}
