package day21

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import se.horv.day21.Day21

class Day21Test {
    @Test
    fun part1() {
        val result = Day21().solve("""029A
980A
179A
456A
379A""".split("\n"), false)

        assertEquals("126384", result)
    }
}