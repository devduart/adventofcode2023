fun main() {
    val input = readInput("Day07")
    println("Part 1: ${part1(input)}")
    println("Part 2: ${part2(input)}")
}
private fun part1(a: List<String>): Long {
    val str = listOf('A', 'K', 'Q', 'J', 'T', '9', '8', '7', '6', '5', '4', '3', '2', 'J')

    class C(val s: String, val h: IntArray, val t: Int, val b: Long) : Comparable<C> {
        override fun compareTo(other: C): Int {
            if (t < other.t) return -1
            if (t > other.t) return 1
            for (i in h.indices) {
                if (h[i] < other.h[i]) return -1
                if (h[i] > other.h[i]) return 1
            }
            return 0
        }
    }

    val cs = a.map { input ->
        val (s, b) = input.split(" ")
        val h = s.map { str.indexOf(it) }
        val g = h.groupingBy { it }.eachCount().toList().sortedByDescending { it.second }
        val t = when {
            g.size == 1 -> 1
            g.size == 2 && g[0].second == 4 -> 2
            g.size == 2 && g[0].second == 3 -> 3
            g.size == 3 && g[0].second == 3 -> 4
            g.size == 3 && g[0].second == 2 && g[1].second == 2 -> 5
            g.size == 4 && g[0].second == 2 -> 6
            else -> 7
        }
        C(s, h.toIntArray(), t, b.toLong())
    }.sortedDescending()

    return cs.withIndex().sumOf { (i, c) -> (i + 1) * c.b }
}
private fun part2(a: List<String>): Long {
    val str = listOf('A', 'K', 'Q', 'T', '9', '8', '7', '6', '5', '4', '3', '2', 'J')

    class C(val s: String, val h: IntArray, val t: Int, val b: Long): Comparable<C> {
        override fun compareTo(other: C): Int {
            if (t < other.t) return -1
            if (t > other.t) return 1
            for (i in h.indices) {
                if (h[i] < other.h[i]) return -1
                if (h[i] > other.h[i]) return 1
            }
            return 0
        }
    }

    val ji = str.indexOf('J')
    val cs = a.map { input ->
        val (s, b) = input.split(" ")
        val h0 = s.map { str.indexOf(it) }
        val t = (0 until ji).minOf { jr ->
            val h = h0.map { if (it == ji) jr else it }
            val g = h.groupingBy { it }.eachCount().toList().sortedByDescending { it.second }
            when {
                g.size == 1 -> 1
                g.size == 2 && g[0].second == 4 -> 2
                g.size == 2 && g[0].second == 3 -> 3
                g.size == 3 && g[0].second == 3 -> 4
                g.size == 3 && g[0].second == 2 && g[1].second == 2 -> 5
                g.size == 4 && g[0].second == 2 -> 6
                else -> 7
            }
        }
        C(s, h0.toIntArray(), t, b.toLong())
    }.sortedDescending()

    return cs.withIndex().sumOf { (i, c) -> (i + 1) * c.b }
}
