import kotlin.math.pow
import kotlin.math.sqrt

typealias CellTool = Pair<Cell, Tool>

class Day22(private val input: List<String>) {

    // sample
//    val depth = 510
//    val targetRow = 10
//    val targetCol = 10


    //actual
    val depth = 11817
    val targetCol = 9
    val targetRow = 751

    val gridExtendRight = 30
    val gridExtendDown = 30


    val MAX_SCORE = Integer.MAX_VALUE

    var grid = Array(targetRow + 1 + gridExtendDown) { Array(targetCol + 1 + gridExtendRight) { Cell() } }

    //    https://rosettacode.org/wiki/A*_search_algorithm
    //    https://www.geeksforgeeks.org/a-search-algorithm/

    // guess 7436 too high, 7433 too high, (2146 too high,2143 too high with gridextend 10) with grideExtend20: 2146
    // 0,0 got it down to less than 1070, cant recall exact - 1064? 1039 was not correct
    // for 30, 30 try 1027 - not correct..  1025 is!

    //    val start = Triple(0,0, Tool.TORCH)
    val start = Pair(Cell(0, 0), Tool.TORCH)
    val finish = Pair(Cell(targetRow, targetCol), Tool.TORCH)


    val openVertices = mutableSetOf(start)
    val closedVertices = mutableSetOf<CellTool>()
    val costFromStart = mutableMapOf(start to 0)
    val estimatedTotalCost = mutableMapOf(start to distanceToTarget(start.first))

    val cameFrom = mutableMapOf<CellTool, CellTool>()  // Used to generate path by back tracking

    // neighbors are the four compass points, plus a tool switch at current position
    fun getNeighbors(cellTool: CellTool): List<CellTool> {
        val neighbors = mutableListOf<CellTool>()
        if (cellTool.first.row + 1 in 0..grid.lastIndex) {
            neighbors.add(CellTool(grid[cellTool.first.row + 1][cellTool.first.col], cellTool.second))
        }

        if (cellTool.first.col + 1 in 0..grid[0].lastIndex) {
            neighbors.add(CellTool(grid[cellTool.first.row][cellTool.first.col + 1], cellTool.second))
        }

        if (cellTool.first.col - 1 in 0..grid[0].lastIndex) {
            neighbors.add(CellTool(grid[cellTool.first.row][cellTool.first.col - 1], cellTool.second))
        }

        if (cellTool.first.row - 1 in 0..grid.lastIndex) {
            neighbors.add(CellTool(grid[cellTool.first.row - 1][cellTool.first.col], cellTool.second))
        }

        // filter out neighbors that don't actually have torch as a tool
        val eligible = neighbors.filter { it.first.cellType.tools.contains(cellTool.second) }.toMutableList()

        val otherTool = cellTool.first.cellType.tools.filterNot { it == cellTool.second }[0]
        eligible.add(Pair(cellTool.first, otherTool))
        println("for cell: ${cellTool.first} eligible neighbors are: $eligible")

        val finalEligible = eligible.filterNot { closedVertices.contains(it) }  //
        println("for cell: ${cellTool.first} filtered final eligible neighbors are: $finalEligible")
        return finalEligible
    }


    fun starSearch() {

        /**
         * Use the cameFrom values to Backtrack to the start position to generate the path
         */
        fun generatePath(currentPos: CellTool, cameFrom: Map<CellTool, CellTool>): List<CellTool> {
            val path = mutableListOf(currentPos)
            var current = currentPos
            while (cameFrom.containsKey(current)) {
                current = cameFrom.getValue(current)
                path.add(0, current)
            }
            return path.toList()
        }

        while (openVertices.size > 0) {

            val currentPos = openVertices.minBy { estimatedTotalCost.getValue(it) }!!

            // Check if we have reached the finish
            if (currentPos == finish) {
                val path = generatePath(currentPos, cameFrom)
                println("currentPos == finish with TOTAL COST ${estimatedTotalCost.getValue(finish)}")
                println("Final path is: $path")
                println("==".repeat(50))
                return
            }

            // Mark the current vertex as closed
            openVertices.remove(currentPos)
            closedVertices.add(currentPos)


            val neighbors = getNeighbors(currentPos)
            neighbors
                    .filterNot { closedVertices.contains(it) }  // Exclude previous visited vertices
                    .forEach { neighbor ->

                        val moveCost = moveCost(currentPos, neighbor)
                        val score = costFromStart.getValue(currentPos) + moveCost
                        if (score < costFromStart.getOrDefault(neighbor, MAX_SCORE)) {
                            if (!openVertices.contains(neighbor)) {
                                openVertices.add(neighbor)
                            }
                            cameFrom.put(neighbor, currentPos) // sets parent
                            costFromStart.put(neighbor, score)
                            estimatedTotalCost.put(neighbor, score + distanceToTarget(neighbor.first))
                        }
                    }
        }
        throw IllegalArgumentException("No Path from Start $start to Finish $finish")
    }

    fun moveCost(currentPos: CellTool, neighbor: CellTool): Int {
        val cost = if (currentPos.second == neighbor.second) 1 else 7
        println("cost to move from ${currentPos.first},${currentPos.second} to ${neighbor.first},${neighbor.second} ------> $cost")
        return cost
    }

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
        printGrid()
        starSearch()
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

    fun distanceToTarget(cell: Cell): Double {
        return sqrt((targetRow - cell.row).toDouble().pow(2) + (targetCol - cell.col).toDouble().pow(2))
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

