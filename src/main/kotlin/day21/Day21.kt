package se.horv.day21

import se.horv.util.Coordinate
import se.horv.util.Solver
import kotlin.math.abs

interface Graph {
    fun getCoordinate(c: Char): Coordinate
}

val CACHE = mutableMapOf<Pair<String, Int>, Long>()

class NumericKeypad: Graph {
    override fun getCoordinate(c: Char): Coordinate = when(c) {
        '0' -> Coordinate(1,3)
        '1' -> Coordinate(0,2)
        '2' -> Coordinate(1,2)
        '3' -> Coordinate(2,2)
        '4' -> Coordinate(0,1)
        '5' -> Coordinate(1,1)
        '6' -> Coordinate(2,1)
        '7' -> Coordinate(0,0)
        '8' -> Coordinate(1,0)
        '9' -> Coordinate(2,0)
        'A' -> Coordinate(2,3)
        else -> throw IllegalArgumentException("Invalid char $c")
    }

}

class DirectionalKeypad: Graph {
    override fun getCoordinate(c: Char): Coordinate = when (c) {
            '^' -> Coordinate(1,0)
            'A' -> Coordinate(2,0)
            '<' -> Coordinate(0,1)
            'v' -> Coordinate(1,1)
            '>' -> Coordinate(2,1)
            else -> throw IllegalArgumentException("Invalid node $c")
    }

}

class Day21: Solver {
    override fun solve(lines: List<String>, partTwo: Boolean): String {
        val levels = if(!partTwo) 2 else 25
        return lines.sumOf {
            val part1 = findNumberOfKeyPresses(it, levels)
            val part2 = it.split("A").first().toInt()
            part1 * part2
        }.toString()
    }

    private fun findNumberOfKeyPresses(s: String, levels: Int): Long {
        val s1 = translateNumericToDirectional(s)
        return findNumberOfKeyPressesDepthFirst(s1, levels, DirectionalKeypad())
    }

    private fun findNumberOfKeyPressesDepthFirst(s: String, levels: Int, g: DirectionalKeypad): Long {
        val key = s to levels
        if(CACHE.containsKey(key)) {
            return CACHE.getValue(key)
        }
        if(levels == 0) {
            return s.length.toLong()
        }
        var start = 'A'
        var sum = 0L
        for (c in s) {
            val sequence = getPath(start, c, g) + "A"
            sum += findNumberOfKeyPressesDepthFirst(sequence, levels-1, g)
            start = c
        }
        CACHE[key] = sum
        return sum
    }

    private fun translateNumericToDirectional(s: String): String {
        var start = 'A'
        val g = NumericKeypad()
        var ts = ""
        for (c in s) {
            val sequence = getPath(start, c, g) + "A"
            ts += sequence
            start = c
        }
        return ts
    }

    private fun getPath(start: Char, target: Char, g: Graph): String {
        val c1 = g.getCoordinate(start)
        val c2 = g.getCoordinate(target)
        val (xDiff, yDiff) = c1.difference(c2)

        val updo = if (yDiff > 0) {
            "v".repeat(abs(yDiff))
        } else if (yDiff < 0) {
            "^".repeat(abs(yDiff))
        } else {
            ""
        }
        val leri = if (xDiff < 0) {
            "<".repeat(abs(xDiff))
        } else if (xDiff > 0) {
            ">".repeat(abs(xDiff))
        } else {
            ""
        }

        if (updo.isEmpty()) {
            return leri
        } else if (leri.isEmpty()) {
            return updo
        }

        when (g) {
            is NumericKeypad -> {
                if(c1.y == 3 && c2.x == 0) {
                    return updo + leri
                } else if (c1.x == 0 && c2.y == 3) {
                    return leri + updo
                }
            }
            is DirectionalKeypad -> {
                if(c1.x == 0) {
                    return leri + updo
                } else if (c2.x == 0) {
                    return updo + leri
                }
            }
        }

        if(yDiff < 0 && xDiff < 0) {
            return leri + updo
        } else if (yDiff > 0 && xDiff < 0) {
            return leri + updo
        } else if (yDiff > 0 && xDiff > 0) {
            return updo + leri
        } else if (yDiff < 0 && xDiff > 0) {
            return updo + leri
        }

        throw IllegalArgumentException("Not caught case: $c1 $c2")
    }
}