package day02

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import se.horv.day02.Day02

class Day02Test {
    @Test
    fun part1() {
        val result = Day02().solve("""7 6 4 2 1
1 2 7 8 9
9 7 6 2 1
1 3 2 4 5
8 6 4 4 1
1 3 6 7 9""".split("\n"), false)

        assertEquals("2", result)
    }

    @Test
    fun part2() {
        val result = Day02().solve("""7 6 4 2 1
1 2 7 8 9
9 7 6 2 1
1 3 2 4 5
8 6 4 4 1
1 3 6 7 9""".split("\n"), true)

        assertEquals("4", result)
    }
}