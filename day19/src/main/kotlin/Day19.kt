class Day19(private val input: List<String>) {

    val functions2 = Functions2()
    val instructions = mutableMapOf<Int, List<Int>>()

    val boundRegister = input[0].split(" ")[1].toInt()
    var currentRegisters = mutableListOf(1, 0, 0, 0, 0, 0)
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

        while (instructions.keys.contains(ip)) {
            currentRegisters[boundRegister] = ip
            val instruction = instructions[ip]!!
            val result = executeInstruction(instruction)

            val prevIp = ip

            if (currentRegisters[0] != result[0]) {
                println("ip:=$prevIp  start reg was $currentRegisters  - $instruction ---> $result   NEXT IP:${result[boundRegister] + 1}")
            }

            ip = result[boundRegister] + 1
            currentRegisters = result.toMutableList()
        }
        return 0
    }

    private fun executeInstruction(inst: List<Int>): List<Int> {
        val result = functions2.applyFunction(currentRegisters, inst)
        return result
    }

    fun part2(): Int {
        return 0
    }
}
