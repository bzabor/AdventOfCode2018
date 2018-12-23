class Day22(private val input: List<String>) {

    // sample
    val depth = 510
    val targetRow = 10
    val targetCol = 10

    //actual
//    val depth = 11817
//    val targetCol = 9
//    val targetRow = 751


    val gridExtend = 5

    // guess 7436 too high

    var grid = Array(targetRow + 1 + gridExtend) { Array(targetCol + 1 + gridExtend) { Cell() } }

    fun init() {
        for (row in 0..targetRow + gridExtend) {
            for (col in 0..targetCol + gridExtend) {
                val cell = grid[row][col]
                cell.row = row
                cell.col = col
                setGeoLogicIndex(cell)
                setCellErosionLevel(cell)
                setCellType(cell)
            }
        }
        grid[0][0].currentTool = Tool.TORCH
    }


    fun part1(): Int {

        init()
        printGrid()

        process(grid[0][0])

        println("FINAL RISK: ${riskLevel()}")

        return 0
    }

//    Before you go in, you should determine the risk level of the area. For the the rectangle that has a top-left corner
//    of region 0,0 and a bottom-right corner of the region containing the target, add up the risk level of each individual
//    region: 0 for rocky regions, 1 for wet regions, and 2 for narrow regions.

//    In the cave system above, because the mouth is at 0,0 and the target is at 10,10, adding up the risk level of all
//    regions with an X coordinate from 0 to 10 and a Y coordinate from 0 to 10, this total is 114.

    private fun riskLevel(): Int {
        val riskList = grid.flatMap { it.filter { it.row <= targetRow && it.col <= targetCol } }.sumBy { it.cellType.risk }
        return riskList
    }

    private fun setGeoLogicIndex(cell: Cell) {
        cell.geologicIndex = when {
            (cell.row == 0 && cell.col == 0) || (cell.row == targetRow && cell.col == targetCol) -> 0
            cell.row == 0 -> cell.col * 16807
            cell.col == 0 -> cell.row * 48271
            else -> grid[cell.row][cell.col - 1].erosionLevel * grid[cell.row - 1][cell.col].erosionLevel
        }
    }

    private fun setCellErosionLevel(cell: Cell) {
        cell.erosionLevel = (cell.geologicIndex + depth) % 20183
    }

    private fun setCellType(cell: Cell) {
        cell.cellType =
                when (cell.erosionLevel % 3) {
                    0 -> CellType.ROCKY
                    1 -> CellType.WET
                    2 -> CellType.NARROW
                    else -> throw Exception("unknown cell type")
                }
    }

    private fun process(cell: Cell) {

        println("ENTER process for cell: $cell")

        if (cell.row == targetRow && cell.col == targetCol) {
            println("cell is TARGET CELL. RETURNING with pathsum: ${cell.pathSum} and path: ${cell.visited}")
            return
        }

        val neighbors = neighborCells(cell)

        // move to all eligible neighbor cells that are not already in path
        // reset path with shorter path cost if shorter

        var thisCellsTool = cell.currentTool

        for (nextCell in neighbors) {

//            println("trying to step to cell $cell...")

            cell.currentTool = thisCellsTool

            var costOfMove = 0

            if (nextCell.cellType.tools.contains(thisCellsTool)) {

                costOfMove = 1

                // Finally, once you reach the target, you need the torch equipped
                // before you can find him in the dark. The target is always in a rocky region,
                // so if you arrive there with climbing gear equipped, you will need to spend
                // seven minutes switching to your torch.

                if (nextCell.row == targetRow && nextCell.col == targetCol) {
                    println("found neighbor that is the TARGET. current tool is ${cell.currentTool}")

                    if (cell.currentTool != Tool.TORCH) {
                        println("need to switch to TORCH")
                        costOfMove = 8
                        cell.currentTool = Tool.TORCH
                    }
                }

            } else {

                // use other tool
                costOfMove = 8
                cell.currentTool = cell.cellType.tools.filterNot { it == cell.currentTool }[0]

            }

            if (cell.pathSum + costOfMove < nextCell.pathSum || nextCell.pathSum == 0) {
                nextCell.pathSum = cell.pathSum + costOfMove
                nextCell.currentTool = cell.currentTool
                nextCell.visited = cell.visited
                nextCell.visited.add(cell)

                process(nextCell)
            } else {
                // path would be greater than the already-existing path to nextCell, so just ignore
                println("PATH WOULD BE GREATER............IGNORING!")
            }


        }
    }

    private fun neighborCells(cell: Cell): List<Cell> {
//        println("enter neighborCells for cell: $cell")
        val neighbors = mutableListOf<Cell>()

        if (cell.col < grid[0].lastIndex) {
            neighbors.add(grid[cell.row][cell.col + 1])
        }

        if (cell.row < grid.lastIndex) {
            neighbors.add(grid[cell.row + 1][cell.col])
        }

        if (cell.row > 0) {
            neighbors.add(grid[cell.row - 1][cell.col])
        }

        if (cell.col > 0) {
            neighbors.add(grid[cell.row][cell.col - 1])
        }

        // filter out unreachable neighbors
        val eligible =  neighbors.filter { it.cellType.tools.any { it in cell.cellType.tools }}
        val ineligible =  neighbors.filterNot { it.cellType.tools.any { it in cell.cellType.tools }}
//        println("for cell: $cell eligible neighbors are: $eligible  ineligible are: $ineligible")

        // filter out neighbors already in path
        val finalEligible = eligible.filterNot { cell.visited.contains(it) }
//        println("for cell: $cell final eligible neighbors are: $finalEligible")

        return finalEligible
    }

    fun part2(): Int {
        return 0
    }

    private fun printGrid() {
        println("")
        println("-".repeat(50))
        for (cellRow in grid) {
            for (cell in cellRow) {
                val symbol = when {
                    cell.row == 0 && cell.col == 0 -> "M"
                    cell.row == targetRow && cell.col == targetCol -> "T"
                    else -> cell.cellType.symbol
                }
                print("$symbol ")
            }
            println("")
        }
        println("-".repeat(50))
        println("")
    }
}

enum class Tool(val symbol: Char) {
    CLIMBING_GEAR('C'),
    TORCH('T'),
    NEITHER('N')
}

enum class CellType(val symbol: Char, val risk: Int, val tools: List<Tool>) {
    ROCKY('.', 0, listOf(Tool.CLIMBING_GEAR, Tool.TORCH)),
    WET('=', 1, listOf(Tool.CLIMBING_GEAR, Tool.NEITHER)),
    NARROW('|', 2, listOf(Tool.TORCH, Tool.NEITHER))
}

data class Cell(
        var row: Int = 0,
        var col: Int = 0

) {
    var geologicIndex: Int = 0
    var erosionLevel: Int = 0
    var cellType: CellType = CellType.ROCKY
    var currentTool: Tool = Tool.NEITHER
    var pathSum: Int = 0
    var visited = mutableListOf<Cell>()
    override fun toString() = "$row:$col"
}

