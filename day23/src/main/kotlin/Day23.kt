import kotlin.math.abs

class Day23(private val input: List<String>) {

//    pos=<0,0,0>, r=4
//    pos=<1,0,0>, r=1
//    pos=<4,0,0>, r=3
//    pos=<0,2,0>, r=1
//    pos=<0,5,0>, r=3
//    pos=<0,0,3>, r=1
//    pos=<1,1,1>, r=1
//    pos=<1,1,2>, r=1
//    pos=<1,3,1>, r=1

//    [, 0, 0, 0, 4]
//    [, 1, 0, 0, 1]
//    [, 4, 0, 0, 3]
//    [, 0, 2, 0, 1]

    // guess 967 is too high

    val posList = mutableListOf<Node>()

    private fun init() {
        var idx = 0
        for (line in input) {
            val a = line.split("""[^-0-9]+""".toRegex()).drop(1).map{it.toLong()}
            posList.add(Node(a[0], a[1], a[2], a[3]))

//            println(a)
        }
    }

    fun part1(): Int {

        init()

        posList.sortByDescending { it.r }

        posList.forEach { println(it) }

        val largest = posList.take(1)[0]

        val countInRadius = posList.filter {
            abs(largest.x - it.x) +
                    abs(largest.y - it.y) +
                    abs(largest.z - it.z) <= largest.r }.count()


        return countInRadius
    }



    fun part2(): Int {



        return 0
    }
}

class Node(
        val x: Long = 0,
        val y: Long = 0,
        val z: Long = 0,
        val r: Long = 0) {
    override fun toString() = "$x:$y:$z - $r"
}
