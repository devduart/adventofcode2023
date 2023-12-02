fun main() {
    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}

fun part1(input: List<String>) = input.sumOf { it.extractValue() }

fun part2(input: List<String>) = input.sumOf { it.calculateValue() }

fun String.extractValue(): Int =
    (find {it.isDigit()}?.digitToInt() ?:0) * 10 + (findLast { it.isDigit() }?.digitToInt() ?: 0)

private val textToDigit = mapOf(
    "one" to 1, "two" to 2, "three" to 3, "four" to 4, "five" to 5,
    "six" to 6, "seven" to 7, "eight" to 8, "nine" to 9
)
private fun String.calculateValue(): Int =
    findFirstOrLastValue(true) * 10 + findFirstOrLastValue(false)

private fun String.findFirstOrLastValue(isFirst: Boolean): Int {
    val indices = if (isFirst) indices else indices.reversed()
    return indices.asSequence()
        .mapNotNull { index -> getValueAt(index) }
        .firstOrNull() ?: 0
}

private fun String.getValueAt(index: Int): Int? =
    when {
        this[index].isDigit() -> Character.getNumericValue(this[index])
        else -> textToDigit.entries.firstOrNull { startsWith(it.key, index) }?.value
    }