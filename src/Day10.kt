
fun main() {
    val input = readInput("Day10")
    println("Part 1: ${part1(input)}")
    println("Part 2: ${part2(input)}")
}

fun parse(input: List<String>): Pair<Array<Array<Tile>>, Tile> {
    val grid = buildList {
        val emptyLine = (1..input.first().length + 2).map { '.' }.joinToString("")
        add(emptyLine)
        addAll(input.map { ".$it." })
        add(emptyLine)
    }
    var startTile = Tile()
    val tiles = Array(grid[0].length) { Array(grid.size) { Tile() } }
    grid.forEachIndexed { row, line ->
        line.forEachIndexed { col, tileSymbol ->
            var isStart = false
            val moves = when (tileSymbol) {
                'S' -> {
                    isStart = true
                    listOf(Move.Up, Move.Down, Move.Left, Move.Right)
                }
                '|' -> listOf(Move.Up, Move.Down)
                '-' -> listOf(Move.Left, Move.Right)
                'L' -> listOf(Move.Up, Move.Right)
                'J' -> listOf(Move.Up, Move.Left)
                '7' -> listOf(Move.Left, Move.Down)
                'F' -> listOf(Move.Right, Move.Down)
                else -> emptyList()
            }
            val tile = Tile(col, row, moves, isStart)
            tiles[col][row] = tile
            if (isStart) {
                startTile = tile
            }
        }
    }
    return tiles to startTile
}

fun getLoopTiles(tiles: Array<Array<Tile>>, startTile: Tile, move: Move): List<Tile> {
    val loopTiles = mutableListOf(startTile)
    var fromTile = startTile
    var currentMove = move
    while (true) {
        val nextTile = tiles.from(currentMove, fromTile)
        if (!nextTile.match(currentMove)) break
        if (nextTile.isStart) return loopTiles
        loopTiles.add(nextTile)
        fromTile = nextTile
        currentMove = nextTile.moves.firstOrNull { it != currentMove.opposite() } ?: break
    }
    return emptyList()
}

fun loopTiles(tiles: Array<Array<Tile>>, startTile: Tile): List<Tile> {
    return startTile.moves.map { move ->
        getLoopTiles(tiles, startTile, move)
    }.first { it.isNotEmpty() }
}

fun isTileInsideLoop(tile: Tile, loopTiles: List<Tile>): Boolean {
    if (tile in loopTiles) return false
    var count = 0
    var j = loopTiles.lastIndex
    for (i in loopTiles.indices) {
        val pi = loopTiles[i]
        val pj = loopTiles[j]
        if (pi.row > tile.row != pj.row > tile.row &&
            tile.col < (pj.col - pi.col) * (tile.row - pi.row) / (pj.row - pi.row) + pi.col) {
            count++
        }
        j = i
    }
    return count % 2 == 1
}

private fun part1(input: List<String>): Int {
    val (tiles, start) = parse(input)
    val loopTiles = loopTiles(tiles, start)
    return loopTiles.size / 2
}

private fun part2(input: List<String>): Int {
    val (tiles, start) = parse(input)
    val loopTiles = loopTiles(tiles, start)
    val interiorTiles = tiles.sumOf { it.count { tile -> isTileInsideLoop(tile, loopTiles) } }
    return interiorTiles
}

sealed class Move(val dp: Pair<Int, Int>) {
    object Up : Move(0 to -1)
    object Down : Move(0 to 1)
    object Left : Move(-1 to 0)
    object Right : Move(1 to 0)
    fun opposite(): Move = when (this) {
        Up -> Down
        Down -> Up
        Left -> Right
        Right -> Left
    }
}

data class Tile(
    val col: Int = 0,
    val row: Int = 0,
    val moves: List<Move> = emptyList(),
    val isStart: Boolean = false,
) {
    fun match(from: Move) = moves.any { it == from.opposite() }
}

fun Array<Array<Tile>>.from(move: Move, tile: Tile): Tile {
    val (dc, dr) = move.dp
    val col = tile.col + dc
    val row = tile.row + dr
    return this[col][row]
}
