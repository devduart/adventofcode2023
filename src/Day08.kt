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

private fun buildNetworkMap(lines: List<String>): Map<String, Pair<String, String>> =
    lines.associate { line ->
        val (node, connections) = line.split("=").map(String::trim)
        val (left, right) = connections.removeSurrounding("(", ")").split(",").map(String::trim)
        node to (left to right)
    }

private fun countStepsToReachZZZ(networkMap: Map<String, Pair<String, String>>, instructions: String): Int {
    var currentNode = "AAA"
    var stepCount = 0

    while (currentNode != "ZZZ") {
        val (left, right) = networkMap[currentNode] ?: error("Invalid node: $currentNode")
        currentNode = if (instructions[stepCount % instructions.length] == 'L') left else right
        stepCount++
    }

    return stepCount
}

private fun part2(input: List<String>): Long {
    val instructions = input.first()
    val networkMap = buildNetworkMap(input.drop(2))
    return calculateLoops(networkMap, instructions)
        .fold(1L) { acc, loop -> lcm(acc, loop.length.toLong()) }
}

private fun calculateLoops(networkMap: Map<String, Pair<String, String>>, instructions: String): List<Loop> =
    networkMap.keys.filter { it.endsWith("A") }
        .map { startNode -> findLoopForNode(startNode, networkMap, instructions) }

private fun findLoopForNode(startNode: String, networkMap: Map<String, Pair<String, String>>, instructions: String): Loop {
    var currentIndex = 0
    var currentNode = startNode
    val seen = mutableMapOf<P, Int>()

    while (true) {
        val instructionIndex = currentIndex % instructions.length
        val key = P(instructionIndex, currentNode)

        seen[key]?.let { firstIndex ->
            return Loop(firstIndex, currentIndex - firstIndex, currentIndex)
        }

        seen[key] = currentIndex
        val direction = instructions[instructionIndex]
        val (left, right) = networkMap[currentNode] ?: error("Invalid node: $currentNode")
        currentNode = if (direction == 'L') left else right
        currentIndex++
    }
}

private fun gcd(x: Long, y: Long): Long = if (y == 0L) x else gcd(y, x % y)

private fun lcm(x: Long, y: Long): Long = x / gcd(x, y) * y

data class P(val instructionIndex: Int, val node: String)
data class Loop(val firstIndex: Int, val length: Int, val loopEnd: Int)
