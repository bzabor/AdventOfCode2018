class Day13(private val input: List<String>) {

    val gridHeight = input.size
    val gridWidth = 150
    val cellGrid = Array(gridHeight) { Array(gridWidth) { Cell(PathType.EMPTY) } }

    var cartList = mutableListOf<Cart>()

    var done = false
    var allCrashedButOne = false
    var tick = 0
    var cartIdx = 0
    val cartIdPool = "ABCDEFGHIJKLMNOPQRSTUVWXYZZ"

    fun init() {
        for (row in input.indices) {
            for (col in input[row].indices) {
                val c = input[row][col]
                cellGrid[row][col].pathType = pathTypeForChar(c)
                if (c in listOf('^', 'v', '>', '<')) {
                    cartList.add(Cart(cartIdPool[cartIdx++], row, col, directionTypeForChar(c)))
                }
            }
        }
    }

    fun part1(): Int {

        init()

        println("-".repeat(50))
        println("TICK $tick")

        println("")
        println("")

        drawGrid()
        printCartListRowCol()

        while (!allCrashedButOne) {

            tick()

            println("-".repeat(50))
            println("")
            println("TICK $tick")
            println("")
            println("")

            drawGrid()
            printCartListRowCol()

            // check for crashes (carts that share same row, col)
//            crash = checkForCrashes()
        }
        return 0
    }

    private fun tick() {

        for (cart in cartList) {

            // get next position
            var nextRow = cart.row
            var nextCol = cart.col

            when (cart.direction) {
                Direction.NORTH -> nextRow--
                Direction.SOUTH -> nextRow++
                Direction.WEST -> nextCol--
                Direction.EAST -> nextCol++
            }


            // get new direction for next position, initialize to current direction
            var nextDirection = cart.direction
            val nextPosPathType = cellGrid[nextRow][nextCol].pathType

            if (nextPosPathType == PathType.CURVE_FORWARD) {

                if (cart.direction == Direction.NORTH) {
                    nextDirection = Direction.EAST
                } else if (cart.direction == Direction.SOUTH) {
                    nextDirection = Direction.WEST
                } else if (cart.direction == Direction.EAST) {
                    nextDirection = Direction.NORTH
                } else if (cart.direction == Direction.WEST) {
                    nextDirection = Direction.SOUTH
                }

            } else if (nextPosPathType == PathType.CURVE_BACK) {
                if (cart.direction == Direction.NORTH) {
                    nextDirection = Direction.WEST
                } else if (cart.direction == Direction.SOUTH) {
                    nextDirection = Direction.EAST
                } else if (cart.direction == Direction.EAST) {
                    nextDirection = Direction.SOUTH
                } else if (cart.direction == Direction.WEST) {
                    nextDirection = Direction.NORTH
                }

            } else if (nextPosPathType == PathType.INTERSECTION) {

                val turn = cart.nextIntersectionTurn()
                nextDirection = getNextDirectionForTurn(cart.direction, turn)

            } else if (nextPosPathType == PathType.EMPTY) {
                throw Exception("Next position path type cannot be EMPTY")
            }

            // perform 'move'
            cart.row = nextRow
            cart.col = nextCol
            cart.direction = nextDirection


            // check for crashes (carts that share same row, col)
            checkForCrashes()

        }

        if (allCrashedButOne) {
            println("FINAL CART LOCATION: ${cartList.filterNot { it.hasCrashed }[0].xyCoordinates()}")
        }

        // sort cartList
        cartList = cartList.sortedWith(compareBy({ it.row }, { it.col })).toMutableList()

        tick++
    }

    private fun printCartListRowCol() {
        println("")
        for (cart in cartList) {
            println("[${cart.id}] - row:col: ${cart.row}:${cart.col}  current direction: ${cart.direction}")
        }
    }

    private fun checkForCrashes() {
        for (cart in cartList) {
            for (otherCart in cartList.filterNot { it == cart }) {
                if (!cart.hasCrashed && !otherCart.hasCrashed && cart.row == otherCart.row && cart.col == otherCart.col) {
                    println("crash found! coords ${cart.xyCoordinates()}")
                    cart.hasCrashed = true
                    otherCart.hasCrashed = true
                }
            }
        }

        allCrashedButOne = cartList.count { !it.hasCrashed } == 1
    }

    fun getNextDirectionForTurn(currentDirection: Direction, turn: String): Direction {
        var index = Direction.values().indexOf(currentDirection)
        when (turn) {
            "L" -> index++
            "R" -> index--
        }
        if (index < 0) {
            index = 3
        } else if (index > 3) {
            index = 0
        }
        return Direction.values()[index]
    }

    private fun pathTypeForChar(c: Char): PathType {
        return when (c) {
            in listOf('|', '^', 'v') -> PathType.VERTICAL
            in listOf('-', '>', '<') -> PathType.HORIZONTAL
            '/' -> PathType.CURVE_FORWARD
            '\\' -> PathType.CURVE_BACK
            '+' -> PathType.INTERSECTION
            else -> PathType.EMPTY
        }
    }

    private fun directionTypeForChar(c: Char): Direction {
        return when (c) {
            '^' -> Direction.NORTH
            'v' -> Direction.SOUTH
            '>' -> Direction.EAST
            '<' -> Direction.WEST
            else -> throw IllegalArgumentException("Unknown direction")
        }
    }

    private fun drawGrid() {

        return
//        println("")
//        val cartId = 'E'
//        val cart = cartList.first { it.id == cartId }
//        println("GRID FOR TICK $tick cart $cartId")
//
//        val cartRow = cart.row
//        val cartCol = cart.col
//
//        for (row in cartRow - 5..cartRow + 5) {
//            for (col in cartCol - 5..cartCol + 5) {
//
//                if (row < 0 || col < 0 || row > cellGrid.lastIndex || col > cellGrid[row].lastIndex) {
//                    continue
//                }
//
//                val cartsForCell = cartsForCell(row, col)
//
//                if (cartsForCell.isEmpty()) {
//                    print("${cellGrid[row][col]} ")
//                } else if (cartsForCell.size > 1) {
//                    print("X ")
//                } else {
//                    print("${cartsForCell[0]} ")
//                }
//            }
//            println("")
//        }

//        for (row in cellGrid.indices) {
//            for (col in cellGrid[0].indices) {
//
//                val cartsForCell = cartsForCell(row, col)
//
//                if (cartsForCell.isEmpty()) {
//                    print("${cellGrid[row][col]} ")
//                } else if (cartsForCell.size > 1) {
//                    print("X ")
//                } else {
//                    print("${cartsForCell[0]} ")
//                }
//            }
//            println("")
//        }

    }


    private fun cartsForCell(row: Int, col: Int): List<Cart> {
        return cartList.filter { it.row == row && it.col == col }
    }


    fun part2(): Int {


        return 0
    }
}


enum class Direction {
    NORTH, WEST, SOUTH, EAST
}

enum class PathType {
    //(| and -), curves (/ and \), and intersections (+)
    VERTICAL, HORIZONTAL, CURVE_FORWARD, CURVE_BACK, INTERSECTION, EMPTY
}

class Cell(var pathType: PathType) {
    override fun toString(): String {
        return when (pathType) {
            PathType.VERTICAL -> "|"
            PathType.HORIZONTAL -> "-"
            PathType.CURVE_BACK -> "\\"
            PathType.CURVE_FORWARD -> "/"
            PathType.INTERSECTION -> "+"
            PathType.EMPTY -> " "
        }
    }
}


class Cart(val id: Char, var row: Int, var col: Int, var direction: Direction) {

    var turnCount = 0

    var hasCrashed = false

    override fun toString(): String {
        return when (direction) {
            Direction.NORTH -> "^"
            Direction.SOUTH -> "v"
            Direction.WEST -> "<"
            Direction.EAST -> ">"
        }
    }

    fun nextIntersectionTurn(): String {
        val turn = when (turnCount % 3) {
            0 -> "L"
            1 -> "S"
            2 -> "R"
            else -> throw Exception("next turn exception")
        }
        turnCount++
        return turn
    }

    fun xyCoordinates(): String {
        return "$col,$row"
    }
}
