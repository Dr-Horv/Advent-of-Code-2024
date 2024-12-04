package se.horv.day04

import se.horv.util.Coordinate
import se.horv.util.Solver

class Day04 : Solver {
    private fun search(c: Coordinate, dir: Coordinate, s: String, map: Map<Coordinate, Char>): Int {
        val ch = map.getValue(c)
        val newS = s + ch
        return if(newS == "XMAS") {
            1
        } else if (newS.length > 4) {
            0
        } else {
            search(c + dir, dir, newS, map )
        }
    }

    override fun solve(lines: List<String>, partTwo: Boolean): String {
        val map = mutableMapOf<Coordinate, Char>().withDefault { '.' }
        lines.forEachIndexed { y, line ->
            line.forEachIndexed { x, c ->
                map[Coordinate(x, y)] = c
            }
        }

        if(!partTwo) {
            return map.keys.sumOf {
                search(it, Coordinate(1, 0), "", map) +
                        search(it, Coordinate(1, -1), "", map) +
                        search(it, Coordinate(1, 1), "", map) +
                        search(it, Coordinate(0, 1), "", map) +
                        search(it, Coordinate(0, -1), "", map) +
                        search(it, Coordinate(-1, 0), "", map) +
                        search(it, Coordinate(-1, -1), "", map) +
                        search(it, Coordinate(-1, 1), "", map)
            }.toString()
        }

        return map.entries.filter { it.value == 'A' }
            .sumOf {
                val downLeft = map.getValue(it.key + Coordinate(x = -1, y = -1))
                val downRight = map.getValue(it.key + Coordinate(x = 1, y = -1))
                val upLeft = map.getValue(it.key + Coordinate(x = -1, y = 1))
                val upRight = map.getValue(it.key + Coordinate(x = 1, y = 1))
                when {
                    upLeft == 'S' && upRight == 'S' &&
                            downLeft == 'M' && downRight == 'M'-> {
                        1
                    }
                    upLeft == 'M' && upRight == 'M' &&
                            downLeft == 'S' && downRight == 'S'-> {
                        1
                    }
                    upLeft == 'S' && upRight == 'M' &&
                            downLeft == 'S' && downRight == 'M'-> {
                        1
                    }
                    upLeft == 'M' && upRight == 'S' &&
                            downLeft == 'M' && downRight == 'S'-> {
                        1
                    }
                    else -> {
                        0
                    }
                }.toInt()
            }.toString()
    }
}