fun main() {
    val input = readInput("Day09")
    println("Part 1: ${part1(input)}")
    println("Part 2: ${part2(input)}")
}


fun extrapolate(seq: List<Long>, backguard: Boolean): Long {
    var value = 0L
    var factor = 1
    var current = seq
    while (current.any { it != 0L }) {
        if (backguard) {
            value += current.first() * factor
            factor *= -1
        } else {
            value += current.last()
        }
        current = current.windowed(2).map { (a, b) -> b - a }
    }
    return value
}


fun extrapolateSum(input: List<String>, backguard: Boolean) = input.map { line ->
    line.split("\\s+".toRegex()).map { it.toLong() }
}.sumOf {
    extrapolate(it, backguard)
}

private fun part1(input: List<String>): Long = extrapolateSum(input, false)

private fun part2(input: List<String>): Long = extrapolateSum(input, true)


fun <T> checkValue(value: T, expectedValue: T) {
    check(value == expectedValue) {
        "value=$value expected=$expectedValue"
    }
}
