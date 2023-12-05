private fun part1(input: List<String>): Int = input
    .asSequence()
    .map { it.split(": ").last().split(" | ").map { nums -> nums.split(" ").filter { num -> num.isNotBlank() } } }
    .map { (winning, have) -> winning.count { it in have } }
    .filter { it != 0 }
    .sumOf { 1.shl(it - 1) }

private fun part2(input: List<String>): Int {
    val cardCounts = input
        .asSequence()
        .map { it.substringBefore(":").split("Card").last().trim().toInt() }
        .groupingBy { it }
        .eachCount()
        .toMutableMap()

    input
        .map { line ->
            val (cardDetails, numbers) = line.split(": ")
            val cardNumber = cardDetails.split("Card")[1].trim().toInt()
            val (winning, have) = numbers.split(" | ").map { nums -> nums.split(" ").filter { num -> num.isNotBlank() } }
            val matchedInLine = winning.count { it in have }
            cardNumber to matchedInLine
        }
        .forEach { (cardNumber, matchedInLine) ->
            (1..matchedInLine).forEach { j ->
                val idx = cardNumber + j
                cardCounts[idx] = cardCounts.getOrDefault(idx, 0) + cardCounts.getOrDefault(cardNumber, 0)
            }
        }

    return cardCounts.values.sum()
}

fun main() {
    val input = readInput("Day04")
    println("Part 1: ${part1(input)}")
    println("Part 2: ${part2(input)}")
}
