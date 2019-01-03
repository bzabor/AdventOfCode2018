class Day21(private val input: List<String>) {
    val functions2 = Functions2()
    val instructions = mutableMapOf<Int, List<Int>>()

    val boundRegister = input[0].split(" ")[1].toInt()

    var count = 0L
    var currentRegisters = mutableListOf(0, 0, 0, 0, 0, 0)
    var ip = 0

    private fun init() {
        var idx = 0
        for (line in input) {
            if (line.contains("#")) continue
            val parts = line.split(" ")
            instructions.put(idx, listOf(functions2.opCodeInt(parts[0]), parts[1].toInt(), parts[2].toInt(), parts[3].toInt()))
            idx++
        }
        println("Instructions: $instructions")
    }


    fun part1(): Int {
        init()

        count = 0L

        var prevR3 = 0

        val r3Vals = mutableListOf<Int>()


        while (instructions.keys.contains(ip)) {


            currentRegisters[boundRegister] = ip
            val instruction = instructions[ip]!!
            val result = executeInstruction(instruction)

            if (ip == 28) {
                print("count: $count ")
                println("ip:=$ip  start  $currentRegisters  ----  [${functions2.funcMapName[instruction[0]]}, ${instruction[1]}, ${instruction[2]}, ${instruction[3]}]   --->    $result}  r3ValsSize: ${r3Vals.size} ")

                if (r3Vals.contains(result[3])) {
                    println("at count: $count  r3 is repeating:  ${result[3]}")
                    break
                } else {
                    r3Vals.add(result[3])
                    prevR3 = result[3]
                }
            }

//            if (ip == 28 || show) {
//                show = true
//                print("count: $count ")
//                println("ip:=$ip  start  $currentRegisters  ----  [${functions2.funcMapName[instruction[0]]}, ${instruction[1]}, ${instruction[2]}, ${instruction[3]}]   --->    $result} ")
//            }

            ip = result[boundRegister] + 1
            currentRegisters = result.toMutableList()
            count++
        }

        println("STOPPED with ip= $ip final count: $count")
        println("Maximum r3 value before repeats: ${r3Vals.max()}  prevR3: $prevR3")
        return 0
    }

    private fun executeInstruction(inst: List<Int>): List<Int> {
        return functions2.applyFunction(currentRegisters, inst)
    }

    fun part2(): Int {
        return 0
    }
}
