import kotlin.math.max


data class Game(val id: Int, val subsets: List<Map<String, Int>>)

fun main() {
    val input = readInput("Day02")
    part1(input).println()
    part2(input).println()
}

private fun part1(games: List<String>) =
    games.sumOf { gameString ->
        gameString.toGame().let { game ->
            if (game.isPossibleWith(maxCubes)) game.id else 0
        }
    }

private fun part2(input: List<String>) =
    input.sumOf { gameString ->
        gameString.toGame().calculatePower()
    }

private fun String.toGame(): Game {
    val (gameId, details) = split(": ")
    val id = gameId.filter { it.isDigit() }.toInt()
    val subsets = details.split("; ").map { subset ->
        subset.split(", ").associate {
            val (count, color) = it.split(" ")
            color to count.toInt()
        }
    }
    return Game(id, subsets)
}

private fun Game.isPossibleWith(maxCubes: Map<String, Int>): Boolean =
    subsets.all { subset -> subset.all { (color, count) -> count <= maxCubes[color]!! } }

private fun Game.calculatePower(): Int {
    val maxCubeCount = subsets
        .flatMap { it.entries }
        .groupingBy { it.key }
        .fold(0) { acc, (_, count) -> max(acc, count) }

    return maxCubeCount.getOrElse("red") { 0 } * maxCubeCount.getOrElse("green") { 0 } * maxCubeCount.getOrElse("blue") { 0 }
}


private val maxCubes = mapOf("red" to 12, "green" to 13, "blue" to 14)