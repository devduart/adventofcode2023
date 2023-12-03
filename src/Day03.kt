fun main() {
    val input = readInput("Day03")
    val grid = input.asGrid()
    val (part1, part2) = grid.processSchematic()
    println(part1)
    println(part2)
}
data class Coord(val row: Int, val col: Int)

private fun List<String>.asGrid() = this.map { it.toCharArray() }

private fun List<CharArray>.processSchematic(): Pair<Int, Int> {
    val coords = mutableMapOf<Coord, MutableList<Int>>()
    var part1 = 0

    this.forEachIndexed { r, row ->
        row.forEachIndexed { c, ch ->
            if (ch.isDigit()) {
                val (num, coord) = this.findNumberAndCoord(Coord(r, c))
                coord?.let {
                    part1 += num
                    coords.getOrPut(it) { mutableListOf() }.add(num)
                }
            }
        }
    }

    val part2 = coords.values.filter { it.size == 2 }.sumOf { it[0] * it[1] }
    return part1 to part2
}

private fun List<CharArray>.findNumberAndCoord(start: Coord): Pair<Int, Coord?> {
    val (row, col) = start
    val num = StringBuilder()
    var coord: Coord? = null
    var currentCol = col
    while (currentCol < this[row].size && this[row][currentCol].isDigit()) {
        num.append(this[row][currentCol])
        coord = coord ?: this.findSpecial(Coord(row, currentCol))
        this[row][currentCol] = '.'
        currentCol++
    }
    return num.toString().toInt() to coord
}

private fun List<CharArray>.findSpecial(coord: Coord): Coord? {
    val directions = listOf(-1 to -1, -1 to 0, -1 to 1, 0 to -1, 0 to 1, 1 to -1, 1 to 0, 1 to 1)
    return directions.asSequence()
        .map { (dr, dc) -> Coord(coord.row + dr, coord.col + dc) }
        .firstOrNull { (r, c) ->
            r in this.indices && c in this[0].indices && !this[r][c].isDigit() && this[r][c] != '.'
        }
}