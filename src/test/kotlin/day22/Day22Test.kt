package day22

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import se.horv.day22.Day22

class Day22Test {
    @Test
    fun part1() {
        val result = Day22().solve("""1
10
100
2024""".split("\n"), false)

        assertEquals("37327623", result)
    }

    @Test
    fun part2() {
        val result = Day22().solve("""1
2
3
2024""".split("\n"), true)

        assertEquals("23", result)
    }
}