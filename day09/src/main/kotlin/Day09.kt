class Day09(private val input: List<String>) {


    private val circle = arrayListOf(0)
    private val numberOfPlayers = 439
    private val marblesToPlay = 7130700
    private val playerScores: MutableMap<Int, Long> = (1..numberOfPlayers).associate { it to 0L }.toMutableMap()

    private var currentPosition = 0
    private var currentMarble = 0
    private var currentPlayer = 0
    private var nextMarble = 0


    fun part1(): Long {

        for (i in 1..marblesToPlay) {
            playTurn()
        }

        val highScore = printScoresReturnHighest()
        return highScore
    }

    private fun playTurn() {

        var nextPosition = 0

        currentMarble = ++nextMarble

        currentPlayer = if (currentMarble % numberOfPlayers == 0) numberOfPlayers else currentMarble % numberOfPlayers

//        println("currentMarble now: $currentMarble   currentPosition: $currentPosition   circlesize: ${circle.size}  currentPlayer: $currentPlayer")


        if (currentMarble % 10000 == 0) {
            println("CurrentMarble: $currentMarble")
        }

        if (currentMarble % 100000 == 0) {
           printScoresReturnHighest()
        }

        if (currentMarble % 23 == 0) {

            var removeIdx = currentPosition - 7
            if (removeIdx < 0) {
                removeIdx = circle.size + removeIdx   // adding a negative number here to subtract
                println("adjusted removeIdx: $removeIdx")
            }
            val removedValue = circle.removeAt(removeIdx)

            playerScores[currentPlayer] = playerScores[currentPlayer] as Long + currentMarble.toLong() + removedValue.toLong()

            currentPosition = removeIdx

            if (currentPosition > circle.lastIndex) {
                currentPosition = 0
            }

        } else {
            nextPosition = getNextInsertPosition()

            if (nextPosition <= circle.lastIndex) {
                circle.add(nextPosition, currentMarble)
            } else {
                circle.add(currentMarble)
            }
            currentPosition = nextPosition
        }
        // printCircle()
    }

    private fun getNextInsertPosition(): Int {
        var nextPosition = currentPosition + 1
        if (nextPosition > circle.lastIndex) {
            nextPosition = 0
        }
        return nextPosition + 1
    }

    private fun printCircle() {
        print("[${currentPlayer}] - ")
        circle.mapIndexed { idx, it -> if (idx == currentPosition) print("(${it}) ") else  print("${it} ") }
        println("")
    }

    private fun printScoresReturnHighest(): Long {
        println("FINAL SCORES:")
        val hiMap = playerScores.toList().sortedByDescending { (_, value) -> value }.take(10).toMap()
        hiMap.forEach{t, u -> println("$t : $u")}
        return hiMap.toList().first().second
    }


    fun part2(): Int {


        return 0
    }
}


class Node<Int>(value: Int) {
    var value: Int = value
    var next: Node<Int>? = null
    var previous: Node<Int>? = null
}



