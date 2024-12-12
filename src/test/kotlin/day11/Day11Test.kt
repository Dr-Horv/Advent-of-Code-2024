package day11

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import se.horv.day11.Day11

class Day11Test {
    @Test
    fun part1() {
        val result = Day11().solve("""125 17""".split("\n"), false)

        assertEquals("55312", result)
    }
}