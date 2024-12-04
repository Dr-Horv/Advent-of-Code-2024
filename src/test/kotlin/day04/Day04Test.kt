package day04

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import se.horv.day04.Day04

class Day04Test {

    @Test
    fun part1() {
        val result = Day04().solve("""MMMSXXMASM
MSAMXMSMSA
AMXSXMAAMM
MSAMASMSMX
XMASAMXAMM
XXAMMXXAMA
SMSMSASXSS
SAXAMASAAA
MAMMMXMMMM
MXMXAXMASX""".split("\n"), false)

        assertEquals("18", result)
    }


    @Test
    fun part2() {
        val result = Day04().solve("""MMMSXXMASM
MSAMXMSMSA
AMXSXMAAMM
MSAMASMSMX
XMASAMXAMM
XXAMMXXAMA
SMSMSASXSS
SAXAMASAAA
MAMMMXMMMM
MXMXAXMASX""".split("\n"), true)

        assertEquals("9", result)
    }
}