import kotlin.math.abs
import kotlin.math.min

class Day22(private val input: List<String>) {

    // sample
//    val depth = 510
//    val targetRow = 10
//    val targetCol = 10

//    https://www.geeksforgeeks.org/a-search-algorithm/

    //actual
    val depth = 11817
    val targetCol = 9
    val targetRow = 751

    var minPathSum = 1_000_000
    //    val pathSumLimit = 1070
    val pathSumLimit = 1600

    val gridExtendRight = 40
    val gridExtendDown = 40

    // guess 7436 too high, 7433 too high, (2146 too high,2143 too high with gridextend 10) with grideExtend20: 2146
    // 0,0 got it down to less than 1070, cant recall exact - 1064? 1039 was not correct
    // 50, 50
    // for 30, 30 guesstng 1027 - not correct




    var grid = Array(targetRow + 1 + gridExtendDown) { Array(targetCol + 1 + gridExtendRight) { Cell() } }

    fun init() {
        for (row in 0..targetRow + gridExtendDown) {
            for (col in 0..targetCol + gridExtendRight) {
                val cell = grid[row][col]
                cell.row = row
                cell.col = col
                setGeoLogicIndex(cell)
                setCellErosionLevel(cell)
                setCellType(cell)
            }
        }
    }


    fun part1(): Int {

        init()
//        printGrid()

        process(grid[0][0], Tool.TORCH, mutableSetOf(Triple(0, 0, Tool.TORCH)), 0)

//        println("FINAL RISK: ${riskLevel()}")

        println("")
        println("FINAL MIN path sum now -------------------------------------------------------------------- $minPathSum")
        println("")

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

    var count = 0

    fun distanceToTarget(cell: Cell): Int {
        return abs(targetRow - cell.row) + abs(targetCol - cell.col)
    }


    private tailrec fun process(cell: Cell, currentTool: Tool, visited: MutableSet<Triple<Int, Int, Tool>>, switchCount: Int) {

//        println("ENTER process for cell: $cell having currentTool: ${currentTool} pathSum: ${cell.pathSum}  switchCount:$switchCount")


        if (cell.pathSum > pathSumLimit) {
//            println("PATHSUM OF ${cell.pathSum} exceeds $pathSumLimit. RETURNING..")
            return
        }

        var nextSwitchCount = switchCount

        val nextVisited = visited.toMutableSet()
        nextVisited.add(Triple(cell.row, cell.col, currentTool))

        if (cell.row == targetRow && cell.col == targetCol) {
            println("cell is TARGET CELL.")

            if (currentTool == Tool.TORCH) {
                minPathSum = min(minPathSum, cell.pathSum)
                println("AND CURRENT TOOL IS TORCH.RETURNING with currentTool: ${currentTool} pathSum: ${cell.pathSum}")// and path: ${visited} ")
                println("min path sum now ----------------------------------------------------------- $minPathSum")
                println("min path sum now ----------------------------------------------------------- $minPathSum")
                println("min path sum now ----------------------------------------------------------- $minPathSum")
                println("min path sum now ----------------------------------------------------------- $minPathSum")
                println("")
                return
            } else {
                nextSwitchCount++
                cell.pathSum += 7
                process(cell, Tool.TORCH, nextVisited, nextSwitchCount)
                return
            }
        }

        var mustSwitch = false
        val neighbors = neighborCells(cell, nextVisited, currentTool)

        val mutableNeighbors = neighbors.toMutableList()



        while (mutableNeighbors.isNotEmpty()) {

            var nextCell: Cell = mutableNeighbors[0]

            var minf = Integer.MAX_VALUE

            for (neighbor in mutableNeighbors) {
                if (distanceToTarget(neighbor) + cell.distanceToNeighbor(neighbor, currentTool) < minf) {
                    minf = distanceToTarget(neighbor)
                    nextCell = neighbor
                }
            }

            mutableNeighbors.remove(nextCell)

            if (nextCell.cellType.tools.contains(currentTool)) {
//                println("no switch was needed for neighbor $nextCell")
//                println("cell: $cell has pathsum: ${cell.pathSum} and costOfMove: 1 . compare to nextcell: $nextCell pathsum of ${nextCell.pathSum}")

                if (cell.pathSum + 1 < nextCell.pathSum || nextCell.pathSum == 0) {
                    nextCell.pathSum = cell.pathSum + 1
//                    println("Going to move to nextCell: $nextCell using currentTool: $currentTool  pathSUm: ${nextCell.pathSum} ") //and path:${nextCell.visited} ")


                    process(nextCell, currentTool, nextVisited, nextSwitchCount)


                }

            } else {
                mustSwitch = true
            }

        }

//            print("trying to step from cell: $cell to cell $nextCell...")


        if (mustSwitch) {
            // switch to other tool IF NOT VISITED ALREADY
            val nextTool = cell.cellType.tools.filterNot { it == currentTool }[0]
            if (!nextVisited.contains((Triple(cell.row, cell.col, nextTool)))) {
                nextSwitchCount++
//                println("SWITCHED nextTool to: ${nextTool} for current cell: $cell")
                cell.pathSum += 7
                process(cell, nextTool, nextVisited, nextSwitchCount)
            } else {
//                println("Cant switch tool to already visited")
            }
        }
//        println("EXIT process for cell: $cell having currentTool: ${currentTool} pathSum: ${cell.pathSum}  switchCount:$switchCount")
    }

    private fun neighborCells(cell: Cell, visited: MutableSet<Triple<Int, Int, Tool>>, currentTool: Tool): List<Cell> {
//        println("enter neighborCells for cell: $cell")

        val incRow = if (targetRow - cell.row >= 0) 1 else -1
        val incCol = if (targetCol - cell.col >= 0) 1 else -1

        val neighbors = mutableListOf<Cell>()


        if (cell.row + incRow in 0..grid.lastIndex) {
            neighbors.add(grid[cell.row + incRow][cell.col])
        }

        if (cell.col + incCol in 0..grid[0].lastIndex) {
            neighbors.add(grid[cell.row][cell.col + incCol])
        }

        if (cell.col - incCol in 0..grid[0].lastIndex) {
            neighbors.add(grid[cell.row][cell.col - incCol])
        }

        if (cell.row - incRow in 0..grid.lastIndex) {
            neighbors.add(grid[cell.row - incRow][cell.col])
        }

//        if (cell.row < targetRow) {
//            if (cell.row < grid.lastIndex) {
//                neighbors.add(grid[cell.row + 1][cell.col])
//            }
//        } else {
//            if (cell.row > 0) {
//                neighbors.add(grid[cell.row - 1][cell.col])
//            }
//        }
//
//        if (cell.col > targetCol) {
//            if (cell.col > 0) {
//                neighbors.add(grid[cell.row][cell.col - 1])
//            }
//        } else {
//            if (cell.col < grid[0].lastIndex) {
//                neighbors.add(grid[cell.row][cell.col + 1])
//            }
//        }

        // filter out unreachable neighbors
        val eligible = neighbors.filter { it.cellType.tools.any { it in cell.cellType.tools } }
        val ineligible = neighbors.filterNot { it.cellType.tools.any { it in cell.cellType.tools } }
//        println("for cell: $cell eligible neighbors are: $eligible  ineligible are: $ineligible")

        // filter out neighbors already in path
        val finalEligible = eligible.filterNot { visited.contains(Triple(it.row, it.col, currentTool)) }
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
    var pathSum: Int = 0
    override fun toString() = "$row:$col"
    fun distanceToNeighbor(cell: Cell, currentTool: Tool) = if (cell.cellType.tools.contains(currentTool)) 1 else 7
}

