import io.kotlintest.matchers.shouldBe
import io.kotlintest.specs.StringSpec

class FrequencyOffsetTest : StringSpec({

    "FrequencyOffset.calculateOffset for non-empty list returns correct value" {
        val calculator = FrequencyOffset(listOf("+1", "+2", "+3", "-5"))
        calculator.calculateOffset().shouldBe(1)
    }

    "FrequencyOffset.findFirstRepeatFrequency for non-empty list returns correct value" {
        val calculator = FrequencyOffset(listOf("+7", "+7", "-2", "-7", "-4"))

        calculator.findFirstRepeatFrequency().shouldBe(14)
    }

})
