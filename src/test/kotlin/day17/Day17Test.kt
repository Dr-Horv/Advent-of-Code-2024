package day17

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import se.horv.day17.Day17

class Day17Test {
    @Test
    fun part1() {
        val result = Day17().solve("""Register A: 729
Register B: 0
Register C: 0

Program: 0,1,5,4,3,0""".split("\n"), false)

        assertEquals("4,6,3,5,6,3,5,2,1,0", result)
    }

    @Test
    fun part2() {
        val result = Day17().solve("""Register A: 2024
Register B: 0
Register C: 0

Program: 0,3,5,4,3,0""".split("\n"), true)

        assertEquals("117440", result)
    }
}