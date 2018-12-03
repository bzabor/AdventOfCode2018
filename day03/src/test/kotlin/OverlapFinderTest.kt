import io.kotlintest.matchers.shouldBe
import io.kotlintest.specs.StringSpec

class OverlapFinderTest : StringSpec({

    "serialsContainingALetterCountOf counts exact matches only" {
        val overlapFinder = OverlapFinder(listOf("#123 @ 3,2: 5x4"))
        overlapFinder.findOverlapCount().shouldBe(1)
    }


})

