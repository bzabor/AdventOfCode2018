import java.io.File
import kotlin.math.max
import kotlin.math.min

class Day17(private val input: List<String>) {

    val grid = Array(2000) { Array(2000) { Cell() } }
    val spring = Cell(row = 0, col = 500, cellType = CellType.SPRING)
    var iteration = 0
    var maxRow = 0
    var maxCol = 0
    var minRangeFinal = 2000
    var maxRangeFinal = 0
    var streamCount = 0

    private fun init() {
        for (line in input) {
            val xy = line.split(", ")
            val firstVal = xy[0].split("=")[1].toInt()
            val (rangeMin, rangeMax) = xy[1].split("=")[1].split("..").map { it.toInt() }

            if (xy[0].contains("x")) {
                for (row in rangeMin..rangeMax) {
                    grid[row][firstVal].cellType = CellType.CLAY
                    maxRow = max(maxRow, row)
                    maxCol = max(maxCol, firstVal)
                }
            } else {
                for (col in rangeMin..rangeMax) {
                    grid[firstVal][col].cellType = CellType.CLAY
                    minRangeFinal = min(minRangeFinal, rangeMin)
                    maxRangeFinal = max(maxRangeFinal, rangeMax)
                    maxRow = max(maxRow, firstVal)
                    maxCol = max(maxCol, rangeMax)
                }
            }
        }
        grid[spring.row][spring.col] = spring
        println("maxRow: $maxRow   minRangeFinal: $minRangeFinal  maxRangeFinal: $maxRangeFinal")
    }

    fun part1(): Int {
        init()
        val stream = Stream(streamCount, spring.row, spring.col)
        streamCount++
        flow(stream)
        printToFile()
        return grid.flatMap { it.filter { it.row >= 8 && it.cellType in listOf(CellType.WATER_FLOWING, CellType.WATER_RESTING) } }.count()
    }


    private tailrec fun flow(stream: Stream) {
        println("ENTER flow for stream cell: $stream   ITERATION: ${++iteration}  streamCount: $streamCount")

        val row = stream.row
        val col = stream.col

        // try to flow down

        if (row + 1 > maxRow) {
            streamCount--
            println("Stream hit maxRow. STOPPING. StreamCount now: $streamCount")
            return
        }

        if (grid[row + 1][col].cellType == CellType.WATER_FLOWING) {
            streamCount--
            println("Stream hit ALREADY FLOWING WATER STREAN. STOPPING. StreamCount now: $streamCount")
            return
        }

        if (grid[row + 1][col].cellType !in listOf(CellType.CLAY, CellType.WATER_RESTING)) {
            stream.row++
            stream.history.add(Pair(stream.row, stream.col))
            grid[stream.row][stream.col].cellType = CellType.WATER_FLOWING

        } else {

            val (boundedLeft, leftIdx) = bounded(-1, row, col)
            val (boundedRight, rightIdx) = bounded(1, row, col)
            println("boundedLeft: $boundedLeft - $leftIdx  boundedRight: $boundedRight - $rightIdx ")

            for (idx in leftIdx..rightIdx) {
                grid[row][idx].cellType = if (boundedLeft && boundedRight) CellType.WATER_RESTING else CellType.WATER_FLOWING
                stream.history.add(Pair(row, idx))
            }

            if (boundedLeft && !boundedRight) {
                stream.col = rightIdx
            } else if (!boundedLeft && boundedRight) {
                stream.col = leftIdx
            } else if (boundedLeft && boundedRight) {
                stream.row--
            } else if (!boundedLeft && !boundedRight) {
                println(" UNBOUNDED LEFT AND RIGHT AT ROW:COL $row:$col  ------- CREATING NEW STREAM")

                // Current Stream will take left side, new stream will take right
                stream.col = leftIdx
                val newStream = Stream(streamCount, row = stream.row, col = rightIdx)
                streamCount++
                flow(newStream)
            }
        }
        printGridForStream(stream)
        flow(stream)
    }

    private fun bounded(direction: Int, row: Int, col: Int): Pair<Boolean, Int> {
        var idx = col
        while (idx in 0..2000) {
            idx += direction
            if (grid[row][idx].cellType == CellType.CLAY) {
                return Pair(true, idx - direction)
            } else if (grid[row + 1][idx].cellType !in listOf(CellType.CLAY, CellType.WATER_RESTING)) {
                return Pair(false, idx)
            }
        }
        throw Exception("Bounded could not be determined")
    }


    fun part2(): Int {
        return 0
    }



    fun printGridForStream(stream: Stream) {
        println("")
        println("GRID FOR STREAM: $stream")

        for (row in max(0, stream.row - 15) .. stream.row + 25) {
            print("${row} - ")
            for (col in stream.col-20 .. stream.col+20) {
                print("${grid[row][col].cellType.symbol} ")
            }
            println("")
        }
        println("-".repeat(50))
        println("")
        println("")
    }

    fun printToFile() {
        File("output.txt").printWriter().use { out ->
            for (row in 0..grid.lastIndex) {
                out.print("${row} - ")
                for (col in 410..580) {
                    out.print("${grid[row][col].cellType.symbol} ")
                }
                out.println("")
            }
        }
    }
}


enum class CellType(val symbol: Char) {
    SPRING('+'),
    SAND('.'),
    CLAY('#'),
    WATER_RESTING('~'),
    WATER_FLOWING('|')
}

class Cell(
        var row: Int = 0,
        var col: Int = 0,
        var cellType: CellType = CellType.SAND
) {
    override fun toString() = "$row:$col"
}

class Stream(val id: Int, var row: Int = 0, var col: Int = 0) {
    var history = mutableSetOf(Pair(row, col))
    override fun toString() = "(${id})$row:$col"
}
