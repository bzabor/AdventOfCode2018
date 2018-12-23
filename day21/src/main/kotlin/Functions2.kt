class Functions2 {
    
    val DEBUG = false

    // Addition
    // addr (add register) stores into register C the result of adding register A and register B.
    private val addr: (List<Int>, List<Int>) -> List<Int> = {
        before, instructions ->
        val result = before.toMutableList()
        result[instructions[3]] = before[instructions[1]] + before[instructions[2]]
        if (DEBUG) if (DEBUG) println("addr returns: $result")
        result
    }

    // addi (add immediate) stores into register C the result of adding register A and value B.
    private val addi: (List<Int>, List<Int>) -> List<Int> = {
        before, instructions ->
        val result = before.toMutableList()
        result[instructions[3]] = before[instructions[1]] + instructions[2]
        if (DEBUG) println("addi returns: $result")
        result
    }

    // Multiplication
    // mulr (multiply register) stores into register C the result of multiplying register A and register B.
    private val mulr: (List<Int>, List<Int>) -> List<Int> = {
        before, instructions ->
        val result = before.toMutableList()
        result[instructions[3]] = before[instructions[1]] * before[instructions[2]]
        if (DEBUG) println("mulr returns: $result")
        result
    }

    // muli (multiply immediate) stores into register C the result of multiplying register A and value B.
    private val muli: (List<Int>, List<Int>) -> List<Int> = {
        before, instructions ->
        val result = before.toMutableList()
        result[instructions[3]] = before[instructions[1]] * instructions[2]
        if (DEBUG) println("muli returns: $result")
        result
    }

    // Bitwise AND:
    // banr (bitwise AND register) stores into register C the result of the bitwise AND of register A and register B.
    private val banr: (List<Int>, List<Int>) -> List<Int> = {
        before, instructions ->
        val result = before.toMutableList()
        result[instructions[3]] = before[instructions[1]] and before[instructions[2]]
        if (DEBUG) println("banr returns: $result")
        result
    }

    // bani (bitwise AND immediate) stores into register C the result of the bitwise AND of register A and value B.
    private val bani: (List<Int>, List<Int>) -> List<Int> = {
        before, instructions ->
        val result = before.toMutableList()
        result[instructions[3]] = before[instructions[1]] and instructions[2]
        if (DEBUG) println("bani returns: $result")
        result
    }

    // Bitwise OR:
    // borr (bitwise AND register) stores into register C the result of the bitwise AND of register A and register B.
    private val borr: (List<Int>, List<Int>) -> List<Int> = {
        before, instructions ->
        val result = before.toMutableList()
        result[instructions[3]] = before[instructions[1]] or before[instructions[2]]
        if (DEBUG) println("borr returns: $result")
        result
    }


    // bori (bitwise OR immediate) sstores into register C the result of the bitwise OR of register A and value B.
    private val bori: (List<Int>, List<Int>) -> List<Int> = {
        before, instructions ->
        val result = before.toMutableList()
        result[instructions[3]] = before[instructions[1]] or instructions[2]
        if (DEBUG) println("bori returns: $result")
        result
    }

    // Assignment
    // setr (set register) copies the contents of register A into register C. (Input B is ignored.)
    private val setr: (List<Int>, List<Int>) -> List<Int> = {
        before, instructions ->
        val result = before.toMutableList()
        result[instructions[3]] = before[instructions[1]]
        if (DEBUG) println("setr returns: $result")
        result
    }

    // seti (set immediate) stores value A into register C. (Input B is ignored.)
    private val seti: (List<Int>, List<Int>) -> List<Int> = {
        before, instructions ->
        val result = before.toMutableList()
        result[instructions[3]] = instructions[1]
        if (DEBUG) println("seti returns: $result")
        result
    }

    // Greater than
    // gtir (greater-than immediate/register) sets register C to 1 if value A is greater than register B. Otherwise, register C is set to 0.
    private val gtir: (List<Int>, List<Int>) -> List<Int> = {
        before, instructions ->
        val result = before.toMutableList()
        result[instructions[3]] = if (instructions[1] > before[instructions[2]]) 1 else 0
        if (DEBUG) println("gtir returns: $result")
        result
    }

    // gtri (greater-than register/immediate) sets register C to 1 if register A is greater than value B. Otherwise, register C is set to 0.
    private val gtri: (List<Int>, List<Int>) -> List<Int> = {
        before, instructions ->
        val result = before.toMutableList()
        result[instructions[3]] = if (before[instructions[1]] > instructions[2]) 1 else 0
        if (DEBUG) println("gtri returns: $result")
        result
    }

    // gtrr (greater-than register/register) sets register C to 1 if register A is greater than register B. Otherwise, register C is set to 0.
    private val gtrr: (List<Int>, List<Int>) -> List<Int> = {
        before, instructions ->
        val result = before.toMutableList()
        result[instructions[3]] = if (before[instructions[1]] > before[instructions[2]]) 1 else 0
        if (DEBUG) println("gtrr returns: $result")
        result
    }

    // Equality
    // eqir (equal immediate/register) sets register C to 1 if value A is equal to register B. Otherwise, register C is set to 0.
    private val eqir: (List<Int>, List<Int>) -> List<Int> = {
        before, instructions ->
        val result = before.toMutableList()
        result[instructions[3]] = if (instructions[1] == before[instructions[2]]) 1 else 0
        if (DEBUG) println("eqir returns: $result")
        result
    }

    // eqri (equal register/immediate) sets register C to 1 if register A is equal to value B. Otherwise, register C is set to 0.
    private val eqri: (List<Int>, List<Int>) -> List<Int> = {
        before, instructions ->
        val result = before.toMutableList()
        result[instructions[3]] = if (before[instructions[1]] == instructions[2]) 1 else 0
        if (DEBUG) println("eqri returns: $result")
        result
    }

    // eqrr (equal register/register) sets register C to 1 if register A is equal to register B. Otherwise, register C is set to 0.
    private val eqrr: (List<Int>, List<Int>) -> List<Int> = {
        before, instructions ->
        val result = before.toMutableList()
        result[instructions[3]] = if (before[instructions[1]] == before[instructions[2]]) 1 else 0
        if (DEBUG) println("eqrr returns: $result")
        result
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

    val funcMapName = mapOf(
            Pair(0,  "bani"),
            Pair(1,  "addr"),
            Pair(2,  "mulr"),
            Pair(3,  "addi"),
            Pair(4,  "gtri"),
            Pair(5,  "banr"),
            Pair(6,  "borr"),
            Pair(7,  "eqri"),
            Pair(8,  "seti"),
            Pair(9,  "eqrr"),
            Pair(10, "bori"),
            Pair(11, "setr"),
            Pair(12, "eqir"),
            Pair(13, "muli"),
            Pair(14, "gtrr"),
            Pair(15, "gtir")
    )

    fun opCodeInt(opText: String): Int {
        return funcMap.filter { it.value.javaClass.name!!.contains(opText) }.keys.toList()[0]
    }

    fun applyFunction(before: List<Int>, instructions: List<Int>): List<Int> {
        val opCode = instructions[0]
        val f = funcMap[opCode]
        if (f != null) {
            return f(before, instructions)
        }
        throw IllegalArgumentException("No function found for opCode: $opCode")
    }
}
