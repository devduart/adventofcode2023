fun main() {
    val input = readInput("Day11")
    println("Part 1: ${part1(input)}")
    println("Part 2: ${part2(input)}")
}

data class Position(var x: Long, var y: Long)

private fun part1(input: List<String>): Long {
    val galaxies = input.mapIndexed { row, line ->
        line.mapIndexedNotNull { col, ch -> if (ch == '#') Position(row.toLong(), col.toLong()) else null }
    }.flatten()

    val blankRows = input.indices.filter { row -> input[row].all { it == '.' } }.toSet()
    val blankCols = input[0].indices.filter { col -> input.all { it[col] == '.' } }.toSet()

    fun adjustedPosition(position: Position): Position {
        val adjustedX = position.x + blankRows.count { it < position.x }
        val adjustedY = position.y + blankCols.count { it < position.y }
        return Position(adjustedX, adjustedY)
    }

    fun distance(p1: Position, p2: Position): Long {
        val adjustedP1 = adjustedPosition(p1)
        val adjustedP2 = adjustedPosition(p2)
        return kotlin.math.abs(adjustedP1.x - adjustedP2.x) + kotlin.math.abs(adjustedP1.y - adjustedP2.y)
    }

    return galaxies.asSequence().withIndex().sumOf { (i, galaxy1) ->
        galaxies.drop(i + 1).sumOf { galaxy2 -> distance(galaxy1, galaxy2) }
    }
}

private fun part2(input: List<String>): Long {
    val galaxies = input.mapIndexed { row, line ->
        line.mapIndexedNotNull { col, ch -> if (ch == '#') Position(row.toLong(), col.toLong()) else null }
    }.flatten()

    val blankRows = input.indices.filter { row -> input[row].all { it == '.' } }.toSet()
    val blankCols = input[0].indices.filter { col -> input.all { it[col] == '.' } }.toSet()

    fun adjustedDistance(p1: Position, p2: Position): Long {
        val dx = kotlin.math.abs(p1.x - p2.x) + blankRows.count { it in p1.x until p2.x || it in p2.x until p1.x } * 999999
        val dy = kotlin.math.abs(p1.y - p2.y) + blankCols.count { it in p1.y until p2.y || it in p2.y until p1.y } * 999999
        return dx + dy
    }

    return galaxies.asSequence().withIndex().sumOf { (i, galaxy1) ->
        galaxies.drop(i + 1).sumOf { galaxy2 -> adjustedDistance(galaxy1, galaxy2) }
    }
}


