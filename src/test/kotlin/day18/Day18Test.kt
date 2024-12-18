package day18

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import se.horv.day18.Day18

class Day18Test {
    @Test
    fun part1() {
        val result = Day18(12).solve("""5,4
4,2
4,5
3,0
2,1
6,3
2,4
1,5
0,6
3,3
2,6
5,1
1,2
5,5
2,5
6,5
1,4
0,4
6,4
1,1
6,1
1,0
0,5
1,6
2,0""".split("\n"), false)

        assertEquals("22", result)
    }

    @Test
    fun part2() {
        val result = Day18(12).solve("""5,4
4,2
4,5
3,0
2,1
6,3
2,4
1,5
0,6
3,3
2,6
5,1
1,2
5,5
2,5
6,5
1,4
0,4
6,4
1,1
6,1
1,0
0,5
1,6
2,0""".split("\n"), true)

        assertEquals("6,1", result)
    }
}