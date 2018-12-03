
class ChecksumCalculator(private val input: List<String>) {

    fun getChecksum(): Int {
        return input
                .map{
                    row -> row.split("""\s+""".toRegex())
                        .map{item -> Integer.parseInt(item)}
                }
                .sumBy {it.max()!! - it.min()!!}
    }
}
