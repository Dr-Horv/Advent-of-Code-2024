package day10

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import se.horv.day10.Day10

class Day10Test {
    @Test
    fun part1() {

        val result1 = Day10().solve("""...0...
...1...
...2...
6543456
7.....7
8.....8
9.....9""".split("\n"), false)

        assertEquals("2", result1)

        val result2 = Day10().solve("""..90..9
...1.98
...2..7
6543456
765.987
876....
987....""".split("\n"), false)

        assertEquals("4", result2)

        val result = Day10().solve("""89010123
78121874
87430965
96549874
45678903
32019012
01329801
10456732""".split("\n"), false)

        assertEquals("36", result)
    }

    @Test
    fun part2() {
        val result = Day10().solve("""89010123
78121874
87430965
96549874
45678903
32019012
01329801
10456732""".split("\n"), false)

        assertEquals("81", result)
    }
}