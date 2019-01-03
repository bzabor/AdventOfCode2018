import kotlin.math.floor
import kotlin.math.min

class Day24(private val input: List<String>) {

    // guess of 10227 is too low. 10538 is correct

    val regex = Regex("""^(\d+) units each with (\d+) hit points \(?(.*?)\)*\s?with an attack that does (\d+) (\w+) damage at initiative (\d+)""")

    val immuneSystemGroups = mutableListOf<Group>()
    val infectionGroups = mutableListOf<Group>()

    var fightCount = 0
    var boost = 0


    private fun init() {
        var immuneSystem = true
        var immuneId = 1
        var infectionId = 1
        for (line in input) {
            if (line.isEmpty()) {
                continue
            }
            if (line.contains("Immune System")) {
                immuneSystem = true
                continue
            }
            if (line.contains("Infection")) {
                immuneSystem = false
                continue
            }

            val arr = regex.matchEntire(line)
            if (arr != null) {
                val (units, hitPoints, optional, attackPoints, attackType, initiative) = arr.destructured

                var weakList = mutableListOf<String>()
                var immuneList = mutableListOf<String>()

                if (optional.isNotEmpty()) {
                    val optionals = optional.split(';')
                    for (option in optionals) {
                        var type = "immune"
                        if (option.contains("weak")) {
                            type = "weak"
                        }
                        val typeList = option.replace("$type to", "").split(",").map { it.trim() }
                        if (type == "weak") {
                            weakList.addAll(typeList)
                        } else {
                            immuneList.addAll(typeList)
                        }
                    }
                }
                val group = Group(
                        if (immuneSystem) "Immune System Group $immuneId" else "Infection Group $infectionId",
                        units.toInt(),
                        hitPoints.toInt(),
                        weakList,
                        immuneList,
                        attackPoints.toInt(),
                        attackType,
                        initiative.toInt(),
                        if (immuneSystem) "Immune System" else "Infection")
                if (immuneSystem) {
                    immuneSystemGroups.add(group)
                    immuneId++
                } else {
                    infectionGroups.add(group)
                    infectionId++
                }
            }
        }
    }

    fun part1(): Int {
        var done = false
        boost = 0

        while (!done) {
            immuneSystemGroups.clear()
            infectionGroups.clear()
            init()
            fightCount = 0
            applyBoost(++boost)
            done = fight()
        }
        return 0
    }

    private fun applyBoost(boost: Int = 0) {
        immuneSystemGroups.forEach { it.attackDamageAmount += boost }
    }

    private fun fight(): Boolean {
        while (immuneSystemGroups.count() > 0 && infectionGroups.count() > 0) {

            var killsInFight = 0
            fightCount++

//            println("\nFIGHT COUNT: $fightCount   BOOST: $boost \n")

            // 1. target selection
            val allGroups = mutableListOf<Group>()
            allGroups.addAll(immuneSystemGroups)
            allGroups.addAll(infectionGroups)

            clearTargets(allGroups)

            val allGroupsSorted = allGroups.sortedWith(compareBy<Group> { -it.effectivePower() }.thenBy { -it.initiative })

            var selectedTargets = mutableListOf<Group>()
            for (attackGroup in allGroupsSorted) {

                val potentialTargets = if (attackGroup.type == "Immune System") infectionGroups else immuneSystemGroups
                val filteredPotentialTargets = potentialTargets.filterNot { selectedTargets.contains(it) }.filterNot { it.unitCount <= 0 }

                var maxDamageTarget: Group? = null
                var maxDamage = 0
                for (target in filteredPotentialTargets) {
                    val calculatedDamage = calculatedDamage(attackGroup, target)

                    if (calculatedDamage == maxDamage && maxDamageTarget != null && allGroupsSorted.indexOf(target) < allGroupsSorted.indexOf(maxDamageTarget)) {
                        maxDamage = calculatedDamage
                        maxDamageTarget = target
                    } else if (calculatedDamage > maxDamage) {
                        maxDamage = calculatedDamage
                        maxDamageTarget = target
                    }
                }

                if (maxDamageTarget != null) {
                    attackGroup.targetGroup = maxDamageTarget
                    selectedTargets.add(maxDamageTarget)
                }
            }

            // 2. attacking
            val allGroupsSortedByInitiative = allGroupsSorted.sortedByDescending { it.initiative }
            for (attackGroup in allGroupsSortedByInitiative) {
                if (attackGroup.targetGroup == null || attackGroup.unitCount <= 0) {
                    continue
                }
                killsInFight += attack(attackGroup)
            }

            // remove dead groups
            immuneSystemGroups.removeAll { it.unitCount <= 0 }
            infectionGroups.removeAll { it.unitCount <= 0 }

            if (killsInFight == 0) {
                break
            }
        }

        println("\nBATTLE IS DONE")
        println("immuneSystem group count: ${immuneSystemGroups.size} unit count: ${immuneSystemGroups.sumBy { it.unitCount }}")
        println("infectionGroups count: ${infectionGroups.size} unit count: ${infectionGroups.sumBy { it.unitCount }}")
        return immuneSystemGroups.size > 0 && infectionGroups.size == 0
    }

    private fun attack(attackGroup: Group): Int {
        var unitsKilled = 0
        if (attackGroup.targetGroup != null) {
            val targetGroup = attackGroup.targetGroup
            val initialUnitCount = targetGroup!!.unitCount

            if (initialUnitCount <= 0) {
                return 0
            }

            val damage = calculatedDamage(attackGroup, targetGroup!!)

            unitsKilled = min(targetGroup.unitCount, floor(damage / targetGroup.hitPoints.toDouble()).toInt())
            targetGroup.unitCount -= unitsKilled
        }
        return unitsKilled
    }

    // By default, an attacking group would deal damage equal to its effective power to the defending group. However,
    // if the defending group is immune to the attacking group's attack type, the defending group instead takes no damage;
    // if the defending group is weak to the attacking group's attack type, the defending group instead takes double damage.
    private fun calculatedDamage(attackGroup: Group, target: Group): Int {
        val damage: Int
        when {
            target.immunities.contains(attackGroup.damageType) -> damage = 0
            target.weaknesses.contains(attackGroup.damageType) -> damage = 2 * attackGroup.effectivePower()
            else -> damage = attackGroup.effectivePower()
        }
        return damage
    }

    private fun clearTargets(allGroups: List<Group>) {
        allGroups.forEach { it.targetGroup = null }
    }

    fun part2(): Int {
        return 0
    }
}


class Group(val id: String,
            var unitCount: Int,
            val hitPoints: Int,
            val weaknesses: List<String>,
            val immunities: List<String>,
            var attackDamageAmount: Int,
            val damageType: String,
            val initiative: Int,
            val type: String
) {

    var targetGroup: Group? = null

    fun effectivePower(): Int {
        return unitCount * attackDamageAmount
    }

    override fun toString(): String {
        return "$id having $unitCount units "
    }
}
