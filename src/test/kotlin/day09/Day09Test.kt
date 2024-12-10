package day09

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import se.horv.day09.Day09

class Day09Test {
     @Test
     fun part1() {
         val result = Day09().solve("""2333133121414131402""".split("\n"), false)

         assertEquals("1928", result)
     }

    @Test
    fun part2() {
        val result = Day09().solve("""2333133121414131402""".split("\n"), true)

        assertEquals("2858", result)
    }
 }