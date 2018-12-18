import io.kotlintest.matchers.shouldBe
import io.kotlintest.specs.StringSpec

//Fuel cell at  122,79, grid serial number 57: power level -5.
//Fuel cell at 217,196, grid serial number 39: power level  0.
//Fuel cell at 101,153, grid serial number 71: power level  4.

class Day11Test : StringSpec({

    "setPowerLevel with serialNumber 8 has correct power level at specified cell" {
        val day11 = Day11(null)
        day11.setPowerLevel(8)
        day11.cellGrid[3][5].shouldBe(4)
    }

    "setPowerLevel with serialNumber 57 has correct power level at specified cell" {
        val day11 = Day11(null)
        day11.setPowerLevel(57)
        day11.cellGrid[122][79].shouldBe(-5)
    }

    "setPowerLevel with serialNumber 39 has correct power level at specified cell" {
        val day11 = Day11(null)
        day11.setPowerLevel(39)
        day11.cellGrid[217][196].shouldBe(0)
    }

    "setPowerLevel with serialNumber 71 has correct power level at specified cell" {
        val day11 = Day11(null)
        day11.setPowerLevel(71)
        day11.cellGrid[101][153].shouldBe(4)
    }

})

