class Day20(private val input: List<String>) {

    // 220 handles main input
    val gridDim = 220
    val startRow = (gridDim / 2)
    val startCol = (gridDim / 2)

    var grid = Array(gridDim) { Array(gridDim) { Cell() } }

    fun init() {
        for (row in 0 until gridDim) {
            for (col in 0 until gridDim) {
                val cell = grid[row][col]
                cell.row = row
                cell.col = col
                if (cell.isOdd()) {
                    cell.cellType = CellType.UNKNOWN
                } else if (!row.isOdd && !col.isOdd) {
                    cell.cellType = CellType.WALL
                } else if (row.isOdd && col.isOdd) {
                    cell.cellType = CellType.UNVISITED_ROOM
                }
            }
        }
        grid[startRow][startCol].cellType = CellType.START
        println("startRow: $startRow   startCol: $startCol")
        printGrid()
    }

    fun part1(): Int {
        init()

        process(grid[startRow][startCol], input[0].drop(1))

        printGrid()

        buildWalls()

        printGrid()

        reportPaths()

        return 0
    }

    private fun buildWalls() {
        for (row in 0 until gridDim) {
            for (col in 0 until gridDim) {
                if (grid[row][col].cellType == CellType.UNKNOWN) {
                    grid[row][col].cellType = CellType.WALL
                }
            }
        }
    }

    private fun reportPaths() {
        val distanceList = grid.flatMap { it.filter { it.cellType == CellType.VISITED_ROOM } }.sortedBy { it.path.size }
        println("DISTANCES TO CELLS:")
        distanceList.forEach { println("cell: $it    path size: ${it.path.size}") }

        distanceList.forEach { println("cell: $it    path size: ${it.path.size}") }

        println("pass thru at least 1000 rooms: ${distanceList.filter { it.path.size >=1000 }.count()}")
    }

    private fun process(cell: Cell, regex: String) {
        println("ENTER process for cell: $cell and regex: $regex")

        if (regex.isEmpty()) {
            println("REGEX is empty. RETURNING...")
            return
        }

        var char = regex.take(1).toCharArray()[0]

        println("processing: $char")
        if (char == '^') {
            println("starting")
            process(cell, regex.drop(1))
        } else if (char == '$') {
            println("hit $. RETURNING")
            return
        } else if (char in "NEWS") {

            var nextCell = cell
            var nextRow = nextCell.row
            var nextCol = nextCell.col
            var nextRegex = regex

            while (char in "NEWS") {

                println("processing in NEWS: $char")

                when (char) {
                    'N' -> {
                        grid[nextCell.row - 1][nextCell.col].cellType = CellType.DOOR_HORIZONTAL; nextRow = nextRow - 2
                    }
                    'S' -> {
                        grid[nextCell.row + 1][nextCell.col].cellType = CellType.DOOR_HORIZONTAL; nextRow = nextRow + 2
                    }
                    'E' -> {
                        grid[nextCell.row][nextCell.col + 1].cellType = CellType.DOOR_VERTICAL; nextCol = nextCol + 2
                    }
                    'W' -> {
                        grid[nextCell.row][nextCell.col - 1].cellType = CellType.DOOR_VERTICAL; nextCol = nextCol - 2
                    }
                }

                if (grid[nextRow][nextCol].path.isEmpty() || nextCell.path.size + 1 < grid[nextRow][nextCol].path.size) {
                    grid[nextRow][nextCol].path.addAll(nextCell.path)
                    grid[nextRow][nextCol].path.add(nextCell)
                }

                nextCell = grid[nextRow][nextCol]
                nextCell.cellType = CellType.VISITED_ROOM

                if (nextRegex.length == 1) {
                    println("NEWS nextRegex is EMPTY. returning...")
                    return
                }

                nextRegex = nextRegex.drop(1)
                char = nextRegex.take(1).toCharArray()[0]
            }

            process(nextCell, nextRegex)

        } else if (char == '(') {

            val matchingIndex = matchingParenIndex(regex)
            val parenGroup = regex.take(matchingIndex + 1)

            println("parenGroup: $parenGroup")

            val branches = splitOnTopLevelPipes(parenGroup.drop(1).dropLast(1))

            println("branches are: $branches")

            for (branch in branches) {
                process(cell, branch)
            }

            process(cell, regex.drop(matchingIndex + 1))
        }
    }

    private fun splitOnTopLevelPipes(exp: String): List<String> {
        val result = mutableListOf<String>()
        var count = 0
        val builder = StringBuilder()

        for (char in exp) {
            if (char == '(') {
                count++
            } else if (char == ')') {
                count--
            }
            if (char == '|' && count == 0) {
                result.add(builder.toString())
                builder.clear()
            } else {
                builder.append(char)
            }
        }
        if (builder.isNotEmpty()) {
            result.add(builder.toString())
        }
        return result
    }

    private fun matchingParenIndex(exp: String): Int {
        if (exp.take(1) != "(") {
            throw IllegalArgumentException("First char not (, its ${exp.take(1)}")
        }
        var count = 0
        var found = false
        var index = 0
        for ((idx, char) in exp.withIndex()) {
            index = idx
            if (char == '(') {
                count++
            } else if (char == ')') {
                count--
            }
            if (count == 0) {
                found = true
                break
            }
        }
        if (!found) throw Exception("Matching paren not found for $exp")
        return index
    }

    private fun printGrid() {
        println("")
        println("-".repeat(50))
        for (cellRow in grid) {
            for (cell in cellRow) {
                print("${cell.cellType.symbol} ")
            }
            println("")
        }
        println("-".repeat(50))
        println("")
    }

    fun part2(): Int {
        return 0
    }
}

enum class Direction(val symbol: Char) {
    NORTH('N'),
    WEST('W'),
    SOUTH('S'),
    EAST('E')
}

enum class CellType(val symbol: Char) {
    WALL('#'),
    DOOR_VERTICAL('|'),
    DOOR_HORIZONTAL('-'),
    UNVISITED_ROOM('+'),
    START('X'),
    VISITED_ROOM('.'),
    UNKNOWN('?'),
}

data class Cell(
        var row: Int = 0,
        var col: Int = 0,
        var cellType: CellType = CellType.UNKNOWN
) {
    var path: MutableList<Cell> = mutableListOf()
    override fun toString() = "$row:$col"
    fun isOdd(): Boolean {
        return (row + col).isOdd
    }
}

/** Returns if the number is odd */
val Int.isOdd get() = this % 2 != 0
