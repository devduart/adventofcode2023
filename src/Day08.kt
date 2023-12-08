fun main() {
    val input = readInput("Day08")
    println("Part 1: ${part1(input)}")
    println("Part 2: ${part2(input)}")
}


private fun part1(input: List<String>): Int {
    val instructions = input.first()
    val networkMap = buildNetworkMap(input.drop(2))
    return countStepsToReachZZZ(networkMap, instructions)
}

fun buildNetworkMap(lines: List<String>): Map<String, Pair<String, String>> {
    return lines.map { line ->
        val (node, connections) = line.split("=").map { it.trim() }
        val (left, right) = connections.removeSurrounding("(", ")").split(",").map { it.trim() }
        node to (left to right)
    }.toMap()
}

fun countStepsToReachZZZ(networkMap: Map<String, Pair<String, String>>, instructions: String): Int {
    var currentNode = "AAA"
    var stepCount = 0

    while (currentNode != "ZZZ") {
        val (left, right) = networkMap[currentNode] ?: error("Invalid node: $currentNode")
        val direction = instructions[stepCount % instructions.length]
        currentNode = if (direction == 'L') left else right
        stepCount++
    }

    return stepCount
}

private fun part2(input: List<String>): Long {
    val instructions = input.first()
    val networkMap = buildNetworkMap(input.drop(2))
    val loops = calculateLoops(networkMap, instructions)

    return loops.fold(1L) { acc, loop -> lcm(acc, loop.length.toLong()) }
}

fun calculateLoops(networkMap: Map<String, Pair<String, String>>, instructions: String): List<Loop> {
    return networkMap.keys.filter { it.endsWith("A") }.map { startNode ->
        findLoopForNode(startNode, networkMap, instructions)
    }
}

fun findLoopForNode(startNode: String, networkMap: Map<String, Pair<String, String>>, instructions: String): Loop {
    var currentIndex = 0
    var currentNode = startNode
    val seen = HashMap<P, Int>()

    while (true) {
        val instructionIndex = currentIndex % instructions.length
        val key = P(instructionIndex, currentNode)

        if (key in seen) {
            val firstIndex = seen[key]!!
            val loopLength = currentIndex - firstIndex
            return Loop(firstIndex, loopLength, currentIndex)
        }

        seen[key] = currentIndex
        val direction = instructions[instructionIndex]
        val (left, right) = networkMap[currentNode] ?: error("Invalid node: $currentNode")
        currentNode = if (direction == 'L') left else right
        currentIndex++
    }
}

fun gcd(x: Long, y: Long): Long = if (y == 0L) x else gcd(y, x % y)

fun lcm(x: Long, y: Long): Long = x * y / gcd(x, y)

data class P(val instructionIndex: Int, val node: String)
data class Loop(val firstIndex: Int, val length: Int, val loopEnd: Int)
