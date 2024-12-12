package day12

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import se.horv.day12.Day12

class Day12Test {
    @Test
    fun part1() {
        val result = Day12().solve("""RRRRIICCFF
RRRRIICCCF
VVRRRCCFFF
VVRCCCJFFF
VVVVCJJCFE
VVIVCCJJEE
VVIIICJJEE
MIIIIIJJEE
MIIISIJEEE
MMMISSJEEE""".split("\n"), false)

        assertEquals("1930", result)
    }

    @Test
    fun part2() {
        val result1 = Day12().solve("""AAAA
BBCD
BBCC
EEEC""".split("\n"), true)

        assertEquals("80", result1)

        val result = Day12().solve("""RRRRIICCFF
RRRRIICCCF
VVRRRCCFFF
VVRCCCJFFF
VVVVCJJCFE
VVIVCCJJEE
VVIIICJJEE
MIIIIIJJEE
MIIISIJEEE
MMMISSJEEE""".split("\n"), true)

        assertEquals("1206", result)
    }
}