class Day15(private val input: List<String>) {

    val grid = Array(input.size) { Array(input[0].length) { Cell() } }


    var round = 0

    fun init() {
        for (row in input.indices) {
            for (col in input[row].indices) {
                val c = input[row][col]
                grid[row][col].row = row
                grid[row][col].col = col
                grid[row][col].cellType = spotTypeForChar(c)
            }
        }
    }

    private fun spotTypeForChar(c: Char): CellType {
        return when (c) {
            '#' -> CellType.WALL
            'E' -> CellType.ELF
            'G' -> CellType.GOBLIN
            '.' -> CellType.EMPTY
            else -> throw IllegalArgumentException("Unkknown spot type: $c")
        }
    }


    fun part1(): Int {

        init()
        drawGrid()


        var elvesAndGoblinsSize = 0
        var unitCount = 0
        while(gridContainsElvesAndGoblins()) {
            round++
            val elvesAndGoblins = grid.flatMap { it.filter { it.isElfOrGoblin() } }
            elvesAndGoblinsSize = elvesAndGoblins.size
            unitCount = 0
            println("------------------- START ROUND $round with ${elvesAndGoblinsSize} units ---------------------------")
            for (elfGoblin in elvesAndGoblins) {

                // make sure it's still an elf or goblin, it may have been killed and set to empty
                if (elfGoblin.isElfOrGoblin()) {
                    takeTurnForCell(elfGoblin)
                    drawGrid()
                }
                println("---------------------------FINISHED UNIT $unitCount of ${elvesAndGoblins.size} --------------------------")
                unitCount++

            }
            println("---------------------------END ROUND $round -----------------------------")
        }

        val completedRounds = if (unitCount == elvesAndGoblinsSize) round else round--
        println("the final round was round: $round. ")
        println("the number of completed rounds: $completedRounds ")

        val remainingUnits = grid.flatMap { it.filter { it.isElfOrGoblin() } }
        val sumOfHitPoints = remainingUnits.sumBy { it.hitPoints }

        println("There were ${remainingUnits.size} remaining units with a hitpoint sum of $sumOfHitPoints")

        println("completedRounds * hitpoints: ${completedRounds * sumOfHitPoints}")
        println("completedRounds-1 * hitpoints: ${(completedRounds-1) * sumOfHitPoints}")

        return 0
    }

    fun part2(): Int {
        return 0
    }

    private fun gridContainsElvesAndGoblins(): Boolean {
        val (elfCount, goblinCount) = grid.flatMap { it.filter { it.isElfOrGoblin() } }.partition { it.cellType == CellType.ELF }
        println("elfCount: $elfCount   goblinCount: $goblinCount")
        return elfCount.size > 0 && goblinCount.size > 0
    }


    // CHECK FOR ATTCK
    // MOVE, CHECK FOR ATTACK AGAIN

    private fun takeTurnForCell(cell: Cell) {

        println("")
        println("")
        println("")
        println("ENTER takeTurnForCell() for cell: $cell")


        // 1. See if ATTACK is possible

        if (attackIfPossible(cell)) {
            return
        }

        // 2. If ATTACK not possible, try to MOVE

        // find min path to enemy cells
        var minPathToEnemyCell = mutableMapOf<Cell, List<Cell>>()

        val enemyCells = enemyCells(cell)

        for (enemyCell in enemyCells) {

            println("processing cell: $cell  ---   enemyCell $enemyCell")

            // clear all cells accumulated distance, start/end markers, paths
            resetDistanceMarkersPaths()

            // mark start and end cells
            cell.isStartCell = true

            enemyCell.isEndCell = true

            // when steps are done, paths will be in enemy cell
            step(cell)
            print("ENEMY IS: $enemyCell  accumulatedDist: ${enemyCell.accumulatedDistance}")
            println(" ..and enemy path is: ${enemyCell.path}")
            val pathToEnemy = enemyCell.path.toList()

            minPathToEnemyCell[enemyCell] = pathToEnemy
        }

        // Cleanup: reset paths, clear end cell, clear
        resetDistanceMarkersPaths()

        // now choose minPathToEnemyCell with the shortest path to enemy. if a tie, choose the earlier one
        println(minPathToEnemyCell)

        val minPathToEnemy = minPathToEnemyCell.values.filter { it.isNotEmpty() }.sortedBy { it.size }
        println("final minPathToEnemy for cell $cell is $minPathToEnemy")

        // TODO: should be able to check for min in the loop itself

        // actually move, if path not empty
        if (minPathToEnemy.isNotEmpty()) {

            // move will be to 2nd position in path
            val nextCell = minPathToEnemy[0][1]

            val currentRow = cell.row
            val currentCol = cell.col

            val newRow = nextCell.row
            val newCol = nextCell.col

            println("moving from $currentRow:$currentCol to $newRow:$newCol")

            grid[newRow][newCol] = cell
            cell.row = newRow
            cell.col = newCol
            grid[currentRow][currentCol] = Cell(row = currentRow, col = currentCol)


            // 3. now, after move, check if ATTACK is possible again
            if (attackIfPossible(cell)) {
                return
            }
        }

        // DONE

    }

    private fun attackIfPossible(cell: Cell): Boolean {
        val attackableEnemies = neighborCells(cell).filter { it.cellType == cell.enemyType() }.sortedBy { it.hitPoints }
        println("Attackable enemies for cell: $cell are: $attackableEnemies")

        if (attackableEnemies.isNotEmpty()) {

            val enemy = attackableEnemies.first()
            println("Attacking enemy: $enemy with original hitpoints of ${enemy.hitPoints}")
            enemy.hitPoints -= cell.attackPower
            println("hit points now ${enemy.hitPoints}")

            if (enemy.hitPoints <= 0) {
                println("REMOVING ENEMY")
                grid[enemy.row][enemy.col].cellType = CellType.EMPTY
            }
            println("attackIfPossible for cell: $cell returning TRUE")
            return true
        }
        return false
    }

    private fun resetDistanceMarkersPaths() {
        grid.flatMap { it.map { it.accumulatedDistance = 0; it.isStartCell = false; it.isEndCell = false; it.path.clear() } }
    }

    private fun enemyCells(cell: Cell): List<Cell> {
        val enemyType = cell.enemyType()
        var enemies = grid.flatMap { it.filter { it.cellType == enemyType } }
        println("enemy cells for cell $cell are $enemies")
        return enemies

    }


    private fun step(cell: Cell) {

        val neighborCells = neighborCells(cell)
//        println("step for cell: $cell with neighborcells: $neighborCells")

        for (neighbor in neighborCells) {
            if (!neighbor.isStartCell &&
                    (neighbor.cellType == CellType.EMPTY || neighbor.isEndCell) &&
                    (neighbor.accumulatedDistance == 0 ||
                            (!cell.path.contains(neighbor) && cell.accumulatedDistance + 1 < neighbor.accumulatedDistance))) {

                neighbor.accumulatedDistance = cell.accumulatedDistance + 1
                neighbor.path = cell.path.toMutableList()
                neighbor.path.add(cell)

                if (neighbor.isEndCell) {
                    neighbor.path.add(neighbor)
//                    println("steps ended at neighbor cell: $neighbor with accum distance of: ${neighbor.accumulatedDistance}")
                } else {
                    step(neighbor)
                }
            }
        }
    }


    private fun drawGrid() {

        return
//        println("")
//        for (row in grid.indices) {
//            for (col in grid[0].indices) {
//                print("${grid[row][col].cellType.symbol} ")
//            }
//            println("")
//        }
//        println("")
    }

    private fun neighborCells(cell: Cell): List<Cell> {
        // follow read convention of Top, Left, Right, Bottom
//        println("enter neighborCells for cell: $cell")
        return listOf(
                grid[cell.row - 1][cell.col],
                grid[cell.row][cell.col - 1],
                grid[cell.row][cell.col + 1],
                grid[cell.row + 1][cell.col])
    }

    enum class CellType(val symbol: Char) {
        WALL('#'),
        EMPTY('.'),
        GOBLIN('G'),
        ELF('E')
    }

    data class Cell(
            var row: Int = 0,
            var col: Int = 0,
            var cellType: CellType = CellType.EMPTY,
            var accumulatedDistance: Int = 0,
            var hitPoints: Int = 200,
            var isStartCell: Boolean = false,
            var isEndCell: Boolean = false
    ) {
        var path: MutableList<Cell> = mutableListOf()
        val attackPower: Int
        get() = when (cellType) {
            CellType.GOBLIN  -> 3
            CellType.ELF -> 23
            else -> 0
        }
        override fun toString() = "$row:$col"

        fun enemyType(): CellType {
            return when (cellType) {
                CellType.ELF -> CellType.GOBLIN
                CellType.GOBLIN -> CellType.ELF
                else -> throw java.lang.Exception("No enemy type for celltype: $cellType")
            }
        }

        fun isElfOrGoblin(): Boolean {
            return cellType in listOf(CellType.ELF, CellType.GOBLIN)
        }


    }
}
