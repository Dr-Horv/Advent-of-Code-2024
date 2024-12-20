package day06

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import se.horv.day06.Day06

class Day06Test {
    @Test
    fun part1() {
        val result = Day06().solve("""....#.....
.........#
..........
..#.......
.......#..
..........
.#..^.....
........#.
#.........
......#...""".split("\n"), false)

        assertEquals("41", result)
    }

    @Test
    fun part2() {
        val result = Day06().solve("""....#.....
.........#
..........
..#.......
.......#..
..........
.#..^.....
........#.
#.........
......#...""".split("\n"), true)

        assertEquals("6", result)
    }
}