package day07

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import se.horv.day07.Day07

class Day07Test {
    @Test
    fun part1() {
        val result = Day07().solve("""190: 10 19
3267: 81 40 27
83: 17 5
156: 15 6
7290: 6 8 6 15
161011: 16 10 13
192: 17 8 14
21037: 9 7 18 13
292: 11 6 16 20""".split("\n"), false)

        assertEquals("3749", result)
    }

    @Test
    fun part2() {
        val result = Day07().solve("""190: 10 19
3267: 81 40 27
83: 17 5
156: 15 6
7290: 6 8 6 15
161011: 16 10 13
192: 17 8 14
21037: 9 7 18 13
292: 11 6 16 20""".split("\n"), true)

        assertEquals("11387", result)
    }
}