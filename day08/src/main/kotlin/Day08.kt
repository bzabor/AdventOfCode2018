class Day08(private val input: List<String>) {

    private val regex = Regex("""\D+""")
    private var numList: MutableList<Int> = regex.split(input[0]).map { it.toInt() }.toMutableList()
    private var sum = 0

    fun part1(): Int {
        process1()
        return sum
    }


    private fun process1() {

        if (numList.isNotEmpty()) {

            // header info
            val childCount = numList[0]
            val metaDataCount = numList[1]

            numList = numList.drop(2).toMutableList()

            for (child in 0 until childCount) {
                process1()
            }

            sum += numList.take(metaDataCount).sum()
            numList = numList.drop(metaDataCount).toMutableList()
        }
    }


    fun part2(): Int {
        sum = 0
        numList = regex.split(input[0]).map { it.toInt() }.toMutableList()
        sum = process2()
        return sum

    }

    private fun process2(): Int {

//        println("NUMLIST: $numList")

        var sum2 = 0

        if (numList.isNotEmpty()) {

            // header info
            val childCount = numList[0]
            val metaDataCount = numList[1]

//            println("childCountL $childCount  metadatacount: $metaDataCount")

            numList = numList.drop(2).toMutableList()

            val childSumList = mutableListOf(0)
            for (child in 1..childCount) {
//                println("Adding child $child to childSumList")
                childSumList.add(child, process2())
            }

//            println("ChildsumList is: ${childSumList}")

            val metaDataList = numList.take(metaDataCount)
//            println("metadataList: $metaDataList")

            if (childCount == 0) {
                val childCountZeroSum = numList.take(metaDataCount).sum()
//                println("Adding $childCountZeroSum to $sum2")
                sum2 += childCountZeroSum
//                println("sum2 now $sum2")

            } else {
                for (metaData in metaDataList) {
//                    println("looking for entry $metaData in childSumList $childSumList")
                    val foundVal = childSumList.getOrElse(metaData) { 0 }
//                    println("Using value of $foundVal")
                    sum2 += foundVal
                }
            }
            numList = numList.drop(metaDataCount).toMutableList()


        }
//        println("SUM2: $sum2")
        return sum2
    }

    class Node(val childCount: Int, val metaData: List<Int>) {
        var children: MutableList<Node> = mutableListOf()
        override fun toString(): String {
            return "${childCount}:${children.size}:$metaData"
        }
    }
}
