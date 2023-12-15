fun main() {
    val input = readInput("Day12")
    println("Part 1: ${part1(input)}")
    println("Part 2: ${part2(input)}")
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


private fun part2(input: List<String>): Long {
    return input
        .map { parseRow(it) }
        .map { unfold(it) }
        .sumOf { (springs, damage) -> countArrangements2(springs, damage) }
}

private fun countArrangements2(
    springs: String,
    damage: List<Int>,
    cache: MutableMap<Pair<String, List<Int>>, Long> = HashMap()
): Long {
    val key = springs to damage
    cache[key]?.let {
        return it
    }
    if (springs.isEmpty()) return if (damage.isEmpty()) 1 else 0

    return when (springs.first()) {
        '.' -> countArrangements2(springs.dropWhile { it == '.' }, damage, cache)

        '?' -> countArrangements2(springs.substring(1), damage, cache) +
                countArrangements2("#${springs.substring(1)}", damage, cache)

        '#' -> when {
            damage.isEmpty() -> 0
            else -> {
                val thisDamage = damage.first()
                val remainingDamage = damage.drop(1)
                if (thisDamage <= springs.length && springs.take(thisDamage).none { it == '.' }) {
                    when {
                        thisDamage == springs.length -> if (remainingDamage.isEmpty()) 1 else 0
                        springs[thisDamage] == '#' -> 0
                        else -> countArrangements2(springs.drop(thisDamage + 1), remainingDamage, cache)
                    }
                } else 0
            }
        }

        else -> throw IllegalStateException("Invalid springs: $springs")
    }.apply {
        cache[key] = this
    }
}
private fun unfold(input: Pair<String, List<Int>>): Pair<String, List<Int>> =
    (0..4).joinToString("?") { input.first } to (0..4).flatMap { input.second }

private fun parseRow(input: String): Pair<String, List<Int>> =
    input.split(" ").run {
        first() to last().split(",").map { it.toInt() }
    }