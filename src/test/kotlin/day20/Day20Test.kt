package day20

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import se.horv.day20.Day20

class Day20Test {
    @Test
    fun part1() {
        val result = Day20().solve("""###############
#...#...#.....#
#.#.#.#.#.###.#
#S#...#.#.#...#
#######.#.#.###
#######.#.#...#
#######.#.###.#
###..E#...#...#
###.#######.###
#...###...#...#
#.#####.#.###.#
#.#...#.#.#...#
#.#.#.#.#.#.###
#...#...#...###
###############""".split("\n"), false)

        assertEquals("8", result)
    }
}