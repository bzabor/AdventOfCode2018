import io.kotlintest.matchers.shouldBe
import io.kotlintest.specs.StringSpec
import java.io.File


class Day13Test : StringSpec({

    "getNextDirectionForTurn " {
        val day13 = Day13(listOf(""))
        day13.getNextDirectionForTurn(Direction.NORTH, "L").shouldBe(Direction.WEST)
        day13.getNextDirectionForTurn(Direction.EAST, "L").shouldBe(Direction.NORTH)
        day13.getNextDirectionForTurn(Direction.SOUTH, "L").shouldBe(Direction.EAST)
        day13.getNextDirectionForTurn(Direction.WEST, "L").shouldBe(Direction.SOUTH)

        day13.getNextDirectionForTurn(Direction.NORTH, "R").shouldBe(Direction.EAST)
        day13.getNextDirectionForTurn(Direction.EAST, "R").shouldBe(Direction.SOUTH)
        day13.getNextDirectionForTurn(Direction.SOUTH, "R").shouldBe(Direction.WEST)
        day13.getNextDirectionForTurn(Direction.WEST, "R").shouldBe(Direction.NORTH)

        day13.getNextDirectionForTurn(Direction.NORTH, "S").shouldBe(Direction.NORTH)
        day13.getNextDirectionForTurn(Direction.EAST, "S").shouldBe(Direction.EAST)
        day13.getNextDirectionForTurn(Direction.SOUTH, "S").shouldBe(Direction.SOUTH)
        day13.getNextDirectionForTurn(Direction.WEST, "S").shouldBe(Direction.WEST)
    }


    "nextIntersectionTurn " {
        val input = File(ClassLoader.getSystemResource("input.txt").file).readLines()
        val day13 = Day13(input)
        day13.init()

        val cart = day13.cartList[0]

        cart.nextIntersectionTurn().shouldBe("L")
        cart.nextIntersectionTurn().shouldBe("S")
        cart.nextIntersectionTurn().shouldBe("R")

        cart.nextIntersectionTurn().shouldBe("L")
        cart.nextIntersectionTurn().shouldBe("S")
        cart.nextIntersectionTurn().shouldBe("R")

        cart.nextIntersectionTurn().shouldBe("L")
        cart.nextIntersectionTurn().shouldBe("S")
        cart.nextIntersectionTurn().shouldBe("R")
    }

})

