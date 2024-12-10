package se.horv.day10

import se.horv.util.Coordinate
import se.horv.util.Direction
import se.horv.util.Solver

class Day10: Solver {
    override fun solve(lines: List<String>, partTwo: Boolean): String {
        val heightMap = mutableMapOf<Coordinate, Int>().withDefault { -1 }
        lines.forEachIndexed { y, row ->
            row.forEachIndexed { x, h ->
                if(h.isDigit()) {
                    heightMap[Coordinate(x,y)] = h.digitToInt()
                }
            }
        }

        return heightMap.entries
            .filter { it.value == 0 }
            .map {
                val reachable = mutableSetOf<Coordinate>()
                val rating = score(it.key, it.value, heightMap, reachable)
                if(!partTwo) {
                    reachable.size
                } else {
                    rating
                }
            }
            .sum().toString()
    }

    private fun score(c: Coordinate, height: Int, heightMap: Map<Coordinate, Int>, reachable: MutableSet<Coordinate>): Int {
        if(height == 9) {
            reachable.add(c)
            return 1
        }
        val target = height + 1
        return Direction.entries.sumOf {
            val next = c + it
            val h = heightMap.getValue(next)
            if (h == target) {
                val s = score(next, h, heightMap, reachable)
                s
            } else {
                0
            }
        }
    }
}