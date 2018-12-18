class Functions {




    // Addition
    // addr (add register) stores into register C the result of adding register A and register B.
    private val addr: (List<Int>, List<Int>, List<Int>) -> Pair<String, Boolean> = {
        before, instructions, after ->
        val check = before[instructions[1]] + before[instructions[2]]
        var result = check == after[instructions[3]]
        println("addr returns: $result")
        Pair("addr", result)

    }

    // addi (add immediate) stores into register C the result of adding register A and value B.
    private val addi: (List<Int>, List<Int>, List<Int>) -> Pair<String, Boolean> = {
        before, instructions, after ->
        val check = before[instructions[1]] + instructions[2]
        var result = check == after[instructions[3]]
        println("addi returns: $result")
        Pair("addi", result)
    }

    // Multiplication
    // mulr (multiply register) stores into register C the result of multiplying register A and register B.
    private val mulr: (List<Int>, List<Int>, List<Int>) -> Pair<String, Boolean> = {
        before, instructions, after ->
        val check = before[instructions[1]] * before[instructions[2]]
        var result = check == after[instructions[3]]
        println("mulr returns: $result")
        Pair("mulr", result)
    }

    // muli (multiply immediate) stores into register C the result of multiplying register A and value B.
    private val muli: (List<Int>, List<Int>, List<Int>) -> Pair<String, Boolean> = {
        before, instructions, after ->
        val check = before[instructions[1]] * instructions[2]
        var result = check == after[instructions[3]]
        println("muli returns: $result")
        Pair("muli", result)
    }

    // Bitwise AND:
    // banr (bitwise AND register) stores into register C the result of the bitwise AND of register A and register B.
    private val banr: (List<Int>, List<Int>, List<Int>) -> Pair<String, Boolean> = {
        before, instructions, after ->
        val check = before[instructions[1]] and before[instructions[2]]
        var result = check == after[instructions[3]]
        println("banr returns: $result")
        Pair("banr", result)
    }

    // bani (bitwise AND immediate) stores into register C the result of the bitwise AND of register A and value B.
    private val bani: (List<Int>, List<Int>, List<Int>) -> Pair<String, Boolean> = {
        before, instructions, after ->
        val check = before[instructions[1]] and instructions[2]
        var result = check == after[instructions[3]]
        println("bani returns: $result")
        Pair("bani", result)
    }

    // Bitwise OR:
    // borr (bitwise AND register) stores into register C the result of the bitwise AND of register A and register B.
    private val borr: (List<Int>, List<Int>, List<Int>) -> Pair<String, Boolean> = {
        before, instructions, after ->
        val check = before[instructions[1]] or before[instructions[2]]
        var result = check == after[instructions[3]]
        println("borr returns: $result")
        Pair("borr", result)
    }

    // bani (bitwise AND immediate) stores into register C the result of the bitwise AND of register A and value B.
    private val bori: (List<Int>, List<Int>, List<Int>) -> Pair<String, Boolean> = {
        before, instructions, after ->
        val check = before[instructions[1]] or instructions[2]
        var result = check == after[instructions[3]]
        println("bori returns: $result")
        Pair("bori", result)
    }

    // Assignment
    // setr (set register) copies the contents of register A into register C. (Input B is ignored.)
    private val setr: (List<Int>, List<Int>, List<Int>) -> Pair<String, Boolean> = {
        before, instructions, after ->
        val check = before[instructions[1]]
        var result = check == after[instructions[3]]
        println("setr returns: $result")
        Pair("setr", result)
    }

    // seti (set immediate) stores value A into register C. (Input B is ignored.)
    private val seti: (List<Int>, List<Int>, List<Int>) -> Pair<String, Boolean> = {
        before, instructions, after ->
        val check = instructions[1]
        var result = check == after[instructions[3]]
        println("seti returns: $result")
        Pair("seti", result)
    }

    // Greater than
    // gtir (greater-than immediate/register) sets register C to 1 if value A is greater than register B. Otherwise, register C is set to 0.
    private val gtir: (List<Int>, List<Int>, List<Int>) -> Pair<String, Boolean> = {
        before, instructions, after ->
        val check = if (instructions[1] > before[instructions[2]]) 1 else 0
        var result = check == after[instructions[3]]
        println("gtir returns: $result")
        Pair("gtir", result)
    }

    // gtri (greater-than register/immediate) sets register C to 1 if register A is greater than value B. Otherwise, register C is set to 0.
    private val gtri: (List<Int>, List<Int>, List<Int>) -> Pair<String, Boolean> = {
        before, instructions, after ->
        val check = if (before[instructions[1]] > instructions[2]) 1 else 0
        var result = check == after[instructions[3]]
        println("gtri returns: $result")
        Pair("gtri", result)
    }

    // gtrr (greater-than register/register) sets register C to 1 if register A is greater than register B. Otherwise, register C is set to 0.
    private val gtrr: (List<Int>, List<Int>, List<Int>) -> Pair<String, Boolean> = {
        before, instructions, after ->
        val check = if (before[instructions[1]] > before[instructions[2]]) 1 else 0
        var result = check == after[instructions[3]]
        println("gtrr returns: $result")
        Pair("gtrr", result)
    }

    // Equality
    // eqir (equal immediate/register) sets register C to 1 if value A is equal to register B. Otherwise, register C is set to 0.
    private val eqir: (List<Int>, List<Int>, List<Int>) -> Pair<String, Boolean> = {
        before, instructions, after ->
        val check = if (instructions[1] == before[instructions[2]]) 1 else 0
        var result = check == after[instructions[3]]
        println("eqir returns: $result")
        Pair("eqir", result)
    }

    // eqri (equal register/immediate) sets register C to 1 if register A is equal to value B. Otherwise, register C is set to 0.
    private val eqri: (List<Int>, List<Int>, List<Int>) -> Pair<String, Boolean> = {
        before, instructions, after ->
        val check = if (before[instructions[1]] == instructions[2]) 1 else 0
        var result = check == after[instructions[3]]
        println("eqri returns: $result")
        Pair("eqri", result)
    }

    // eqrr (equal register/register) sets register C to 1 if register A is equal to register B. Otherwise, register C is set to 0.
    private val eqrr: (List<Int>, List<Int>, List<Int>) -> Pair<String, Boolean> = {
        before, instructions, after ->
        val check = if (before[instructions[1]] == before[instructions[2]]) 1 else 0
        var result = check == after[instructions[3]]
        println("eqrr returns: $result")
        Pair("eqrr", result)
    }

    val allFunctions = listOf(addr, addi, muli, mulr, banr, bani, borr, bori, setr, seti, gtir, gtri, gtrr, eqir,  eqri, eqrr)

    val funcMap = mapOf(
            Pair(0,  bani),
            Pair(1,  addr),
            Pair(2,  mulr),
            Pair(3,  addi),
            Pair(4,  gtri),
            Pair(5,  banr),
            Pair(6,  borr),
            Pair(7,  eqri),
            Pair(8,  seti),
            Pair(9,  eqrr),
            Pair(10, bori),
            Pair(11, setr),
            Pair(12, eqir),
            Pair(13, muli),
            Pair(14, gtrr),
            Pair(15, gtir)
    )

    fun getCountOfPossibleOps(before: List<Int>, instructions: List<Int>, after: List<Int>): Int {
        var trueCount = 0
        for (f in allFunctions) {
            if (f(before, instructions, after).second) trueCount++
        }
        return trueCount
    }

    fun getSetOfPossibleFunctionNames(before: List<Int>, instructions: List<Int>, after: List<Int>): Set<String> {
        var funcSet = mutableSetOf<String>()
        for (f in allFunctions) {

            val resultPair = f(before, instructions, after)
            if (resultPair.second) {
                funcSet.add(resultPair.first)
            }
        }
        return funcSet
    }

//    fun applyFunction(opCode: Int, before: List<Int>, instructions: List<Int>): List<Int> {
//
//    }
}
