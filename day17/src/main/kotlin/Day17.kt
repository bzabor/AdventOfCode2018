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
        printGridHelp(centerRow = 290, centerCol = 506)
        val stream = Stream(streamCount, spring.row, spring.col)
        streamCount++
        flow(stream)
        return grid.flatMap { it.filter { it.cellType in listOf(CellType.WATER_FLOWING, CellType.WATER_RESTING) } }.count()
    }


    private tailrec fun flow(stream: Stream) {
        println("ENTER flow for stream cell: $stream   ITERATION: ${++iteration}  streamCount: $streamCount")

//        if (iteration > 510) {
//            println("Max iterations hit. returning.....")
//            return
//        }

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
            println("cant flow down..")

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

        printGridHelp(centerRow = row)
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


    private fun printGrid(cols: Int = 120, rows: Int = 50) {
        println("")
        for (row in maxRow-50 .. maxRow) {
            for (col in 400 .. 500) {
                print("${grid[row][col].cellType.symbol} ")
            }
            println("")
        }

//        for (row in 0 .. 14) {
//            for (col in 490..510) {
//                print("${grid[row][col].cellType.symbol} ")
//            }
//            println("")
//        }

        println("maxRow: $maxRow")
        println("-".repeat(50))
        println("")
        println("")
    }


    private fun printGridHelp(cols: Int = 90, rows: Int = 40, centerRow: Int = 300, centerCol: Int = 500) {
        return
//        println("")
//        for (row in max(0,centerRow-rows/2) .. centerRow+rows/2) {
//            print("$row - ")
//            for (col in centerCol-cols/2 .. centerCol+cols/2) {
//                print("${grid[row][col].cellType.symbol} ")
//            }
//            println("")
//        }
//
//        println("maxRow: $maxRow")
//        println("-".repeat(50))
//        println("")
//        println("")
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
    var history = mutableSetOf<Pair<Int, Int>>(Pair(row, col))
    override fun toString() = "(${id})$row:$col"
}
