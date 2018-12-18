

class Day16(private val input: List<String>, val input2: List<String>) {

    val befores = mutableListOf<List<Int>>()
    val afters = mutableListOf<List<Int>>()
    val instructions = mutableListOf<List<Int>>()

    private fun init() {
        for (line in input) {
//            println("line: $line")
            if (line.isEmpty()) {
                continue
            } else if (line.contains("Before")) {
                befores.add(line.substring(9..18).replace(",", "").split(' ').map { it.toInt() })
            } else if (line.contains("After")) {
                afters.add(line.substring(9..18).replace(",", "").split(' ').map { it.toInt() })
            } else {
                instructions.add(line.split(" ").map { it.toInt() })
            }
        }
    }

    fun part1(): Int {

        init()

        var threeOrMore = 0
        for (idx in befores.indices) {

            println("")
            println("BEGIN idx: $idx")
            println("befores: ${befores[idx]}   isntructions: ${instructions[idx]}   afters: ${afters[idx]}")

            val count = Functions().getCountOfPossibleOps(befores[idx], instructions[idx], afters[idx])
            println("count is: $count")

            if (count >= 3) {
                threeOrMore++
            }

            println("Count of 3 or more: $threeOrMore")
        }

        return 0
    }

    var mapCodeToFunction = mutableMapOf<Int, MutableSet<String>>()
    fun part2(): Int {
        println("BEGIN PART 2")

//        init()
//
//        for (idx in befores.indices) {
//
//            println("")
//            println("BEGIN idx: $idx")
//
//            val opCode = instructions[idx][0]
//            println("OPCODE: $opCode   befores: ${befores[idx]}   isntructions: ${instructions[idx]}   afters: ${afters[idx]}")
//
//
//            val funcSet = Functions().getSetOfPossibleFunctionNames(befores[idx], instructions[idx], afters[idx])
//            println("funcSet is: $funcSet")
//
//            var codeFuncSet: MutableSet<String> = mapCodeToFunction.getOrDefault(opCode, mutableSetOf())
//
//            codeFuncSet.addAll(funcSet)
//
//            mapCodeToFunction.put(opCode, codeFuncSet)
//
//            println("set for opcode now: ${ mapCodeToFunction.get(opCode)}")
//
//        }
//
//        println("")
//        mapCodeToFunction.toSortedMap().forEach {
//            println(" ${it.key}   -->  ${it.value}")
//        }

        // ===========================================================================

        // PROCESS PART 2

        // starting with 3, 0, 0, 3

        //input
//        13 0 0 0
//        3 0 2 0
//        8 3 0 1
//        13 0 0 3

        var currentRegister = listOf(3,0,0,3)

        for (line in input2) {
            println("line: $line")

            val instructions = line.split(" ").map{ it.toInt()}

            println("instructions: $instructions")

            val after = Functions2().applyFunction(currentRegister, instructions)

            currentRegister = after
        }




        return 0
    }



}
