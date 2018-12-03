import io.kotlintest.matchers.shouldBe
import io.kotlintest.specs.StringSpec

class ChecksumTest : StringSpec({

    "ChecksumCalculator.getChecksum for single row with same values only returns 0" {
        val calculator = ChecksumCalculator(listOf("5555 5555"))
        calculator.getChecksum().shouldBe(0)
    }

    "ChecksumCalculator.getChecksum for multiple row returns correct checksum" {
        val calculator = ChecksumCalculator(listOf("5555 5555", "4004 4000"))
        calculator.getChecksum().shouldBe(4)
    }

})
