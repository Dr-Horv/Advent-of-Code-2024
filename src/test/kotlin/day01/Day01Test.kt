package day01

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import se.horv.day01.Day01

class Day01Test {

    @Test
    fun part1() {
        val result = Day01().solve(
            """3   4
4   3
2   5
1   3
3   9
3   3""".split("\n")
        )

        assertEquals("11", result)
    }

    @Test
    fun part2() {
        val result = Day01().solve(
            """3   4
4   3
2   5
1   3
3   9
3   3""".split("\n"), true
        )

        assertEquals("31", result)
    }
}