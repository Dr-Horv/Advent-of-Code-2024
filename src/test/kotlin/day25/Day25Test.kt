package day25

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import se.horv.day25.Day25

class Day25Test {
    @Test
    fun part1() {
        val result = Day25().solve("""#####
.####
.####
.####
.#.#.
.#...
.....

#####
##.##
.#.##
...##
...#.
...#.
.....

.....
#....
#....
#...#
#.#.#
#.###
#####

.....
.....
#.#..
###..
###.#
###.#
#####

.....
.....
.....
#....
#.#..
#.#.#
#####""".split("\n"), false)

        assertEquals("3", result)
    }
}