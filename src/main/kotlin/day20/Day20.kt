package se.horv.day20

import se.horv.util.Coordinate
import se.horv.util.Direction
import se.horv.util.Solver
import java.util.*

enum class Tile {
    WALL,
    EMPTY
}

data class State(val coordinate: Coordinate,
                 val step: Int) {
    fun getNeighbours(map: Map<Coordinate, Tile>): Set<State> {
        val neighbours = mutableSetOf<State>()
        for (dir in Direction.entries) {
            val n = this.coordinate + dir
            if(map.getValue(n) == Tile.EMPTY) {
                neighbours.add(State(n, 1))
            }
        }
        return neighbours
    }
}

class Day20: Solver {
    override fun solve(lines: List<String>, partTwo: Boolean): String {
        val map = mutableMapOf<Coordinate, Tile>()
        var start = Coordinate(0, 0)
        var end = Coordinate(0, 0)
        lines.forEachIndexed { y, row ->
            row.forEachIndexed { x, cell ->
                when (cell) {
                    '#' -> map[Coordinate(x, y)] = Tile.WALL
                    '.' -> map[Coordinate(x, y)] = Tile.EMPTY
                    'S' -> {
                        val c = Coordinate(x, y)
                        map[c] = Tile.EMPTY
                        start = c
                    }
                    'E' -> {
                        val c = Coordinate(x, y)
                        map[c] = Tile.EMPTY
                        end = c
                    }
                }
            }
        }

        val isGoal = fun(c: State): Boolean = c.coordinate == end
        val h = fun (s: State): Int = s.coordinate.manhattanDistance(end)
        val startState = State(start, 0)
        val fastest = aStar(startState, isGoal, h, map)
        val cheats: Int = findValidCheats(fastest, if (!partTwo) 2 else 20, 100)
        return cheats.toString()

    }

    private fun findValidCheats(path: List<State>, cheatLength: Int, limit: Int): Int {
        var result = 0
        val costsUpTo = mutableMapOf<Int, Int>()
        val costRemaining = mutableMapOf<Int, Int>()
        val best = path.sumOf { it.step }
        var sum = 0
        for ((i,s) in path.withIndex()) {
            sum += s.step
            costsUpTo[i] = sum
            costRemaining[i] = best - sum
        }
        for((i,c1) in path.withIndex()) {
            val part1 = costsUpTo.getValue(i)
            for (i2 in i + 4..path.lastIndex) {
                val c2 = path[i2]
                val dist = c1.coordinate.manhattanDistance(c2.coordinate)
                if(dist <= cheatLength) {
                    val part2 = costRemaining.getValue(i2)
                    val total = part1 + dist + part2
                    val diff = best - total
                    if(diff >= limit) {
                        result++
                    }
                }
            }
        }
        return result
    }


    private fun aStar(startState: State, isGoal: (State) -> Boolean, h:(State) -> Int, map: Map<Coordinate, Tile>): List<State> {
        val cameFrom = mutableMapOf<State, State>()
        val gScore = mutableMapOf(startState to 0).withDefault { Int.MAX_VALUE }
        val fScore = mutableMapOf(startState to h(startState)).withDefault { Int.MAX_VALUE }
        val openSet = PriorityQueue(fun (s1: State, s2: State): Int =
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

    private fun reconstructPath(cameFrom: Map<State, State>, s: State): List<State> {
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