fun main() {
    val input = readInput("Day11")
    println("Part 1: ${part1(input)}")
    println("Part 2: ${part2(input)}")
}

data class Position(var x: Long, var y: Long)

private fun part1(input: List<String>): Long {
    val galaxies = mutableListOf<Position>()
    val blankRows = mutableSetOf<Int>()
    val blankCols = mutableSetOf<Int>()

    input.forEachIndexed { row, line ->
        line.forEachIndexed { col, ch ->
            if (ch == '#') galaxies.add(Position(row.toLong(), col.toLong()))
        }
    }

    input.indices.filter { row -> input[row].all { it == '.' } }.forEach { blankRows.add(it) }
    input[0].indices.filter { col -> input.all { it[col] == '.' } }.forEach { blankCols.add(it) }

    galaxies.forEach { galaxy ->
        galaxy.x += blankRows.count { it < galaxy.x }
        galaxy.y += blankCols.count { it < galaxy.y }
    }

    var totalDist: Long = 0
    for (i in galaxies.indices) {
        for (j in i until galaxies.size) {
            totalDist += kotlin.math.abs(galaxies[i].x - galaxies[j].x) +
                    kotlin.math.abs(galaxies[i].y - galaxies[j].y)
        }
    }

    return totalDist
}

private fun part2(input: List<String>): Long {
    val galaxies = mutableListOf<Position>()
    val blankRows = mutableSetOf<Int>()
    val blankCols = mutableSetOf<Int>()

    input.forEachIndexed { row, line ->
        line.forEachIndexed { col, ch ->
            if (ch == '#') galaxies.add(Position(row.toLong(), col.toLong()))
        }
    }

    input.indices.filter { row -> input[row].all { it == '.' } }.forEach { blankRows.add(it) }
    input[0].indices.filter { col -> input.all { it[col] == '.' } }.forEach { blankCols.add(it) }

    galaxies.forEach { galaxy ->
        galaxy.x += blankRows.count { it < galaxy.x } * 999999
        galaxy.y += blankCols.count { it < galaxy.y } * 999999
    }

    var totalDist: Long = 0
    for (i in galaxies.indices) {
        for (j in i until galaxies.size) {
            totalDist += kotlin.math.abs(galaxies[i].x - galaxies[j].x) +
                    kotlin.math.abs(galaxies[i].y - galaxies[j].y)
        }
    }

    return totalDist
}

