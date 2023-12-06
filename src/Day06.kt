fun main() {
    val input = readInput("Day06")
    println("Part 1: ${part1(input)}")
    println("Part 2: ${part2(input)}")
}

private fun part1(input: List<String>) =
    parseInput(input)
        .let { (time, distance) ->
            time.zip(distance)
                .countWaysToBeatRecords()
        }

private fun part2(input: List<String>) =
    parseInputForPartTwo(input)
        .let { (time, distance) ->
            calculateWaysToBeatRecord(time.first(), distance.first())
        }

private fun parseInput(input: List<String>) =
    Pair(
        input[0].splitToNumbers(),
        input[1].splitToNumbers()
    )

private fun parseInputForPartTwo(input: List<String>) =
    Pair(
        listOf(input[0].parseToLong()),
        listOf(input[1].parseToLong())
    )

private fun List<Pair<Int, Int>>.countWaysToBeatRecords() =
    map { (t, d) -> calculateWaysToBeatRecord(t.toLong(), d.toLong()) }
        .fold(1L, Long::times)

private fun String.splitToNumbers() =
    split("\\s+".toRegex()).drop(1).map(String::toInt)

private fun String.parseToLong() =
    substringAfter(":").filter { it.isDigit() }.toLong()

private fun calculateWaysToBeatRecord(time: Long, recordDistance: Long) =
    (0L..time).count {
        val speed = it
        val travelTime = time - it
        speed * travelTime > recordDistance
    }
