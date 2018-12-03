class PassphraseChecker(private val input: List<String>) {

    fun getValidCount(): Int {
        return input
                .map { row -> row.split("""\s+""".toRegex())
                }
                .filter { it.toSet().size == it.size }
                .size
    }

    fun getValidAnagramCount(): Int {
       val x = input
                .map { row -> row.split("""\s+""".toRegex()) }

        val y = x.forEach {

//            it.map { item -> item.toCharArray().sort() }.map{ item ->  item}

            it.forEach {
                item -> item.toCharArray().sort()
            }
//            return 0
        }

        return  0
    }
}
