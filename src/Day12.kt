object Day12Solver {

    fun solve(input: List<String>): Pair<Int, Long> {
        val parsedInput = input.map { parseRow(it) }
        return part1(parsedInput) to part2(parsedInput)
    }

    private fun part1(input: List<Pair<String, List<Int>>>): Int =
        input.sumOf { (springs, groupSizes) -> springs.generateAndCount(groupSizes) }

    private fun part2(input: List<Pair<String, List<Int>>>): Long =
        input.map { it.unfold() }.sumOf { (springs, damage) -> springs.countArrangementsWithDamage(damage) }

    private fun String.generateAndCount(groupSizes: List<Int>): Int {
        val unknownPositions = mapIndexedNotNull { index, c -> if (c == '?') index else null }
        return if (unknownPositions.isEmpty()) {
            if (isValid(groupSizes)) 1 else 0
        } else {
            generateCombinations(toCharArray(), unknownPositions, groupSizes)
        }
    }

    private fun generateCombinations(springs: CharArray, positions: List<Int>, groupSizes: List<Int>, index: Int = 0): Int =
        if (index == positions.size) {
            if (String(springs).isValid(groupSizes)) 1 else 0
        } else {
            listOf('#', '.').sumOf { char ->
                springs[positions[index]] = char
                generateCombinations(springs, positions, groupSizes, index + 1)
            }
        }

    private fun String.isValid(groupSizes: List<Int>): Boolean =
        trim('.').split(".").filter { it.isNotEmpty() }.map { it.length } == groupSizes

    private fun Pair<String, List<Int>>.unfold(): Pair<String, List<Int>> =
        (0..4).joinToString("?") { first } to (0..4).flatMap { second }

    private fun String.countArrangementsWithDamage(damage: List<Int>, cache: MutableMap<Pair<String, List<Int>>, Long> = HashMap()): Long {
        val key = this to damage
        return cache.getOrPut(key) {
            when {
                isEmpty() -> if (damage.isEmpty()) 1 else 0
                first() == '.' -> dropWhile { it == '.' }.countArrangementsWithDamage(damage, cache)
                first() == '?' -> {
                    val sub = substring(1)
                    sub.countArrangementsWithDamage(damage, cache) + "#$sub".countArrangementsWithDamage(damage, cache)
                }
                else -> handleHashCase(damage, cache)
            }
        }
    }

    private fun String.handleHashCase(damage: List<Int>, cache: MutableMap<Pair<String, List<Int>>, Long>): Long =
        if (damage.isEmpty()) 0
        else {
            val thisDamage = damage.first()
            val remainingDamage = damage.drop(1)
            when {
                thisDamage > length || take(thisDamage).any { it == '.' } -> 0
                thisDamage == length -> if (remainingDamage.isEmpty()) 1 else 0
                this.getOrNull(thisDamage) == '#' -> 0
                else -> drop(thisDamage + 1).countArrangementsWithDamage(remainingDamage, cache)
            }
        }


    private fun parseRow(input: String): Pair<String, List<Int>> =
        input.split(" ").let { it.first() to it.last().split(",").map(String::toInt) }
}

fun main() {
    val input = readInput("Day12")
    val (part1, part2) = Day12Solver.solve(input)
    println("Part 1: $part1")
    println("Part 2: $part2")
}
