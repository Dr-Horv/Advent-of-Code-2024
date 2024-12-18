package se.horv.day18

import se.horv.util.Coordinate
import se.horv.util.Direction
import se.horv.util.Solver
import java.util.*
import kotlin.math.abs

enum class Tile {
    EMPTY,
    BYTE
}

fun Coordinate.getNeighbours(map: Map<Coordinate, Tile>): List<Coordinate> {
    val neighbours = mutableListOf<Coordinate>()
    for (dir in Direction.entries) {
        val n = this + dir
        if(map.getValue(n) == Tile.EMPTY) {
            neighbours.add(n)
        }
    }

    return neighbours
}

class Day18(private val steps: Int = 1024) : Solver {
    override fun solve(lines: List<String>, partTwo: Boolean): String {
        val bytes = lines.map {
            val parts = it.split(",")
            Coordinate(parts[0].toInt(), parts[1].toInt())
        }

        val maxX = bytes.maxOf { it.x }
        val maxY = bytes.maxOf { it.y }
        val map = mutableMapOf<Coordinate, Tile>().withDefault { Tile.BYTE }
        for (y in 0..maxY) {
            for (x in 0..maxX) {
                map[Coordinate(x, y)] = Tile.EMPTY
            }
        }

        for (i in 0 until steps) {
            map[bytes[i]] = Tile.BYTE
        }

        val start = Coordinate(0,0)
        val goal = Coordinate(maxX,maxY)
        val isGoal = fun (c: Coordinate): Boolean = c == goal
        val h = fun (c: Coordinate): Int = abs(goal.x - c.x) + abs(goal.y - c.y)
        if(!partTwo) {
            val path = aStar(start, isGoal, h, map)
            return (path.size-1).toString()
        }

        val search = fun (m: Map<Coordinate, Tile>): List<Coordinate> = aStar(start, isGoal, h, m)

        return binarySearch(bytes.drop(steps), map, search)
    }

    private fun binarySearch(
        bytes: List<Coordinate>,
        map: Map<Coordinate, Tile>,
        search: (Map<Coordinate, Tile>) -> List<Coordinate>
    ): String {
        var left = 0
        var right = bytes.lastIndex
        while (left <= right) {
            val mid = (left + right) / 2
            var test = false
            try {
                search(createNewMap(mid, bytes, map))
                test = true
                search(createNewMap(mid + 1, bytes, map))
                left = mid + 1
            } catch (e: IllegalArgumentException) {
                if (test) {
                    val culprit = bytes[mid + 1]
                    return "${culprit.x},${culprit.y}"
                } else {
                    right = mid - 1
                }
            }
        }

        throw IllegalArgumentException("Failed binary search")
    }

    private fun createNewMap(bytesUntil: Int, bytes: List<Coordinate>, map: Map<Coordinate, Tile>): Map<Coordinate, Tile> {
        val m = map.toMutableMap().withDefault { Tile.BYTE }
        for (i in 0 .. bytesUntil) {
            m[bytes[i]] = Tile.BYTE
        }
        return m
    }

    private fun aStar(startState: Coordinate, isGoal: (Coordinate) -> Boolean, h:(Coordinate) -> Int, map: Map<Coordinate, Tile>): List<Coordinate> {
        val cameFrom = mutableMapOf<Coordinate, Coordinate>()
        val gScore = mutableMapOf(startState to 0).withDefault { Int.MAX_VALUE }
        val fScore = mutableMapOf(startState to h(startState)).withDefault { Int.MAX_VALUE }
        val openSet = PriorityQueue(fun (s1: Coordinate, s2: Coordinate): Int =
            fScore.getValue(s1).compareTo(fScore.getValue(s2))
        )

        openSet.add(startState)


        while (openSet.isNotEmpty()) {
            val curr = openSet.poll()
            if(isGoal(curr)) {
                return reconstructPath(cameFrom, curr)
            }

            for (neighbour in curr.getNeighbours(map)) {
                val tentativeGScore = gScore.getValue(curr) + 1
                if (tentativeGScore < gScore.getValue(neighbour)) {
                    cameFrom[neighbour] = curr
                    gScore[neighbour] = tentativeGScore
                    fScore[neighbour] = tentativeGScore + h(neighbour)
                    if(!openSet.contains(neighbour)) {
                        openSet.add(neighbour)
                    }
                }
            }


        }

        throw IllegalArgumentException("Failure!")
    }

    private fun reconstructPath(cameFrom: Map<Coordinate, Coordinate>, s: Coordinate): List<Coordinate> {
        val path = mutableListOf(s)
        var curr = s
        while (cameFrom.containsKey(curr)) {
            val n = cameFrom[curr]!!
            path.add(n)
            curr = n
        }
        return path.reversed()
    }

}