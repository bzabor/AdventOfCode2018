class Day18(private val input: List<String>) {

    val gridDim = 50
    var grid = Array(gridDim) { Array(gridDim) { Cell() } }

    var minutes = 0

    fun init() {
        for (row in input.indices) {
            for (col in input[row].indices) {
                val c = input[row][col]
                grid[row][col] = Cell(row, col, cellTypeForChar(c))
            }
        }
    }

    fun part1(): Int {
        init()

        printGrid(grid)


        do {

            minutes++
            grid = getNextGrid()
            printGrid(grid)

        } while (minutes < 10000)

        val trees = grid.flatMap { it.filter { it.cellType == CellType.TREES} }.count()
        val lumberyards = grid.flatMap { it.filter { it.cellType == CellType.LUMBERYARD} }.count()

        return trees * lumberyards
    }


    fun part2(): Int {
        return 0
    }



    private fun getNextGrid(): Array<Array<Cell>> {
        val nextGrid = Array(gridDim) { Array(gridDim) { Cell() } }
        for (row in grid.indices) {
            for (col in grid[row].indices) {
                nextGrid[row][col] = nextCell(grid[row][col])
            }
        }
        return nextGrid
    }

    //    An open acre will become filled with trees if three or more adjacent acres contained trees. Otherwise, nothing happens.
    //    An acre filled with trees will become a lumberyard if three or more adjacent acres were lumberyards. Otherwise, nothing happens.
    //    An acre containing a lumberyard will remain a lumberyard if it was adjacent to at least one other lumberyard and at least one acre containing trees. Otherwise, it becomes open.

    private fun nextCell(cell: Cell): Cell {
        val neighbors = neighborCells(cell)
        val nextCell = when  {
            cell.cellType == CellType.OPEN          ->  if (neighbors.count { it.cellType == CellType.TREES } >= 3) Cell(cell.row, cell.col, CellType.TREES) else cell
            cell.cellType == CellType.TREES         ->  if (neighbors.count { it.cellType == CellType.LUMBERYARD } >= 3) Cell(cell.row, cell.col, CellType.LUMBERYARD) else cell
            cell.cellType == CellType.LUMBERYARD    ->  if (neighbors.count { it.cellType == CellType.LUMBERYARD } >= 1 && neighbors.count { it.cellType == CellType.TREES } >= 1) cell else Cell(cell.row, cell.col, CellType.OPEN)
            else -> throw IllegalArgumentException("Unknown cellType for cell: $cell")
        }
        return nextCell
    }


    private fun neighborCells(cell: Cell): List<Cell> {
       return grid.flatMap { it.filter { it.row in cell.row-1..cell.row+1 && it.col in cell.col-1..cell.col+1 }. filterNot { it.row == cell.row && it.col == cell.col} }
    }




    private fun cellTypeForChar(c: Char): CellType {
        return when (c) {
            '.' -> CellType.OPEN
            '|' -> CellType.TREES
            '#' -> CellType.LUMBERYARD
            else -> CellType.OPEN
        }
    }

    private fun printGrid(aGrid: Array<Array<Cell>>) {
//        println("")
//        println("-".repeat(50))


//        for (row in aGrid) {
//            for (col in row) {
//                print("${col.cellType.symbol} ")
//            }
//            println("")
//        }

//        if (minutes % 100 == 0) {
            print("Minutes: $minutes  -----   ")
            val trees = grid.flatMap { it.filter { it.cellType == CellType.TREES} }.count()
            val lumberyards = grid.flatMap { it.filter { it.cellType == CellType.LUMBERYARD} }.count()
            println("trees: $trees   lumberyards: $lumberyards   value: ${trees * lumberyards}")
//        }

//        println("-".repeat(50))
//        println("")
//        println("")
    }
}

enum class CellType(val symbol: Char) {
    OPEN('.'),
    LUMBERYARD('#'),
    TREES('|')
}

data class Cell(
        var row: Int = 0,
        var col: Int = 0,
        var cellType: CellType = CellType.OPEN
) {
    override fun toString() = "$row:$col"
}
