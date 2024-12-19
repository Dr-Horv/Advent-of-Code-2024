package day19

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import se.horv.day19.Day19

class Day19Test {
    @Test
    fun part1() {
        val result = Day19().solve("""r, wr, b, g, bwu, rb, gb, br

brwrr
bggr
gbbr
rrbgbr
ubwu
bwurrg
brgr
bbrgwb""".split("\n"), false)

        assertEquals("6", result)
    }

    @Test
    fun part2() {
        val result = Day19().solve("""r, wr, b, g, bwu, rb, gb, br

brwrr
bggr
gbbr
rrbgbr
ubwu
bwurrg
brgr
bbrgwb""".split("\n"), true)

        assertEquals("16", result)
    }
}