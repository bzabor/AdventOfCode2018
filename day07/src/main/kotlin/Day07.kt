class Day07(private val input: List<String>) {

    private val possibleNexts = mutableSetOf<Node>()
    private val path = mutableListOf<Node>()
    private val nodeMap = mutableMapOf<Char, Node>()

    fun part1(): String {
        buildNodeMap()
        possibleNexts.addAll(nodeMap.values.filter { it.required.size == 0 })
        val node = getNext()
        if (node != null) {
            process(node)
        }
        return path.map { it.id }.joinToString(separator = "")
    }

    fun part2(): Int {
        nodeMap.clear()
        buildNodeMap()
        possibleNexts.clear()
        possibleNexts.addAll(nodeMap.values.filter { it.required.size == 0 })

        val workingNodes = mutableListOf<Node>()

        var seconds = 0
        while (seconds < 1000) {

            // if not first second, tick the clock:
            // - decrement working node timers
            // for each working node where timer is 0
            // -   remove it from all nodes' required sets
            // -   remove it from the nodeMap
            // -   remove it from working nodes

            if (seconds > 0) {
                workingNodes.forEach{ it.timer--}
                val completedNodes = workingNodes.filter { it.timer == 0 }
                workingNodes.removeAll(completedNodes)
                completedNodes.forEach {completedNode ->
                    nodeMap.remove(completedNode.id)
                    nodeMap.values.forEach { it.required.remove(completedNode) }
                }
            }

            var freeWorkers = 5 - nodeMap.values.filter { it.isWorking }.count()

            // assign each worker a next node
            while (possibleNexts.isNotEmpty() && freeWorkers > 0) {
                val node = getNext()
                if (node != null) {
                    node.isWorking = true
                    workingNodes.add(node)
                    freeWorkers--
                    possibleNexts.addAll(node.next)
                } else {
                    break
                }
            }

            print("IN SECOND $seconds  ")
            workingNodes.forEach{ print("  [${it.id}]: ${it.timer}") }
            println("")

            if (workingNodes.size == 0) return seconds

            seconds++
        }
        return 0
    }

    private fun buildNodeMap() {
        for (line in input) {
            val (required, next) = line.split(" ").slice(listOf(1, 7))
            val requiredNode = nodeMap.getOrPut(required[0]) { Node(required[0]) }
            val nextNode = nodeMap.getOrPut(next[0]) { Node(next[0]) }
            requiredNode.next.add(nextNode)
            nextNode.required.add(requiredNode)
        }
    }

    private fun getNext(): Node? {
        val requirementsMetNodes = possibleNexts.filter { it.required.size == 0 }
        if (requirementsMetNodes.size > 0) {
            val node = requirementsMetNodes.sortedBy { it.id }.first()
            possibleNexts.remove(node)
            return node
        }
        return null
    }

    private fun process(node: Node) {

        // add it to the path
        path.add(node)

        // add its nexts to possibleNexts
        possibleNexts.addAll(node.next)

        // remove it from all nodes' required sets
        nodeMap.values.forEach { it.required.remove(node) }

        // remove it from the nodeMap
        nodeMap.remove(node.id)

        // if possibleNexts exist, get next, recurse
        if (possibleNexts.isNotEmpty()) {
            val node = getNext()
            if (node != null) {
                process(node)
            }
        }
    }
}

class Node(val id: Char) {
    val alphabet: CharRange = 'A'..'Z'
    val next = mutableSetOf<Node>()
    val required = mutableSetOf<Node>()
    var isWorking = false
    var timer = alphabet.indexOf(id) + 1 + 60
}
