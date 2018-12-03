
import io.kotlintest.matchers.shouldBe
import io.kotlintest.specs.StringSpec

class SerialCheckerTest : StringSpec({

    "serialsContainingALetterCountOf counts exact matches only" {
        val checker = SerialChecker(listOf("a", "aa", "aaa", "aaaa"))
        checker.serialsContainingALetterCountOf(2).shouldBe(1)
    }

    "serialsContainingALetterCountOf finds multiple matches" {
        val checker = SerialChecker(listOf("a", "aa", "aaa", "aaaa", "bb", "bbb"))
        checker.serialsContainingALetterCountOf(2).shouldBe(2)
    }

    "serialsContainingALetterCountOf matches count passed in value" {
        val checker = SerialChecker(listOf("a", "aa", "aaa", "aaaa", "bb"))
        checker.serialsContainingALetterCountOf(3).shouldBe(1)
    }

    "getCommonSerialString excludes non-matching letter" {
        val checker = SerialChecker(listOf("abcdefg", "abceefg"))
        checker.getCommonSerialString().shouldBe("abcefg")
    }
})
