
class Day04(private val input: List<String>) {

    val guardMap = mutableMapOf<String, Guard>()
    val sorted = input.toTypedArray().sorted()

    fun part1(): Int {

        processInput()

        val maxGuard = guardMostAsleep()
        var maxIdx = 0
        var minuteMax = 0
        for ((idx, minute) in maxGuard!!.asleep.withIndex()) {
            println("$idx -  $minute")
            if (minute > minuteMax) {
                minuteMax =  minute
                maxIdx = idx
            }
        }
        return maxGuard.id.toInt() * maxIdx
    }

    fun part2(): Int {

        processInput()

        var maxGuard: Guard? = null
        var maxGuardMaxMinutesIdx = 0
        var maxMinutes = 0

        for (guard in guardMap.values) {

            for ((idx, minutes) in guard.asleep.withIndex()) {
                if (minutes > maxMinutes) {
                    maxMinutes = minutes
                    maxGuardMaxMinutesIdx = idx
                    maxGuard = guard
                }
            }
        }
        return maxGuard.id.toInt() * maxGuardMaxMinutesIdx
    }

    private fun processInput() {

        val guardRegex = Regex("""#\d+""")
        val minuteRegex = Regex(""":\d\d""")

        var guard: Guard? = null
        var sleeps = 0
        var wakes = 0

        for (line in sorted) {
            println(line)

            var match = guardRegex.find(line)
            if(match != null) {
                var guardId = match.value.drop(1)
                println("Guardid: $guardId")
                guard = guardMap.getOrPut(guardId) {Guard(guardId)}

            } else {
                match = minuteRegex.find(line)
                if (match != null) {
                    if (line.contains("asleep")) {
                        sleeps = match.value.drop(1).toInt()
                    } else {
                        wakes = match.value.drop(1).toInt()

                        for (idx in sleeps until wakes) {
                            guard!!.asleep[idx]++
                        }
                        println(guard!!.asleep)
                    }
                }
            }
        }
    }

    private fun guardMostAsleep(): Guard? {
        var maxGuard: Guard? = null
        for (guard in guardMap.values) {
            if (maxGuard == null || guard.asleep.sum() > maxGuard.asleep.sum()) {
                maxGuard = guard
            }
        }
        return maxGuard
    }

    data class Guard(val id: String) {
        var asleep = Array(60) {0}
    }
}
