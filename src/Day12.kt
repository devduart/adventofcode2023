fun main() {
    val input = readInput("Day12")
    println("Part 1: ${part1(input)}")
}


private fun part1(input: List<String>): Int {
    return countArrangements(input)
}

fun countArrangements(input: List<String>): Int {
    return input.sumOf { row ->
        val (springs, groups) = row.split(' ')
        val groupSizes = groups.split(',').map { it.toInt() }
        generateAndCount(springs, groupSizes)
    }
}

fun generateAndCount(springs: String, groupSizes: List<Int>): Int {
    val unknownPositions = springs.mapIndexedNotNull { index, c -> if (c == '?') index else null }
    return if (unknownPositions.isEmpty()) {
        if (isValid(springs, groupSizes)) 1 else 0
    } else {
        generateCombinations(springs.toCharArray(), unknownPositions, 0, groupSizes)
    }
}

fun generateCombinations(springs: CharArray, positions: List<Int>, index: Int, groupSizes: List<Int>): Int {
    if (index == positions.size) {
        return if (isValid(String(springs), groupSizes)) 1 else 0
    }
    var count = 0
    springs[positions[index]] = '#'
    count += generateCombinations(springs, positions, index + 1, groupSizes)
    springs[positions[index]] = '.'
    count += generateCombinations(springs, positions, index + 1, groupSizes)
    return count
}

fun isValid(springs: String, groupSizes: List<Int>): Boolean {
    val actualSizes = springs.trim('.').split(".").filter { it.isNotEmpty() }.map { it.length }
    return actualSizes == groupSizes
}