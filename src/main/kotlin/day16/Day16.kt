package se.horv.day16

import se.horv.util.Coordinate
import se.horv.util.Direction
import se.horv.util.Solver
import java.util.PriorityQueue
import kotlin.math.abs

enum class Tile {
    WALL,
    EMPTY
}


data class State(val coordinate: Coordinate, val facing: Direction) {
    private fun turnRight(): State = State(coordinate, facing.turnRight())
    private fun turnLeft(): State = State(coordinate, facing.turnLeft())
    fun getNeighbours(map: Map<Coordinate, Tile>): Set<Pair<Int,State>> {
        val neighbours = mutableSetOf(
            1000 to turnRight(),
            1000 to turnLeft()
        )

        val n = this.coordinate + this.facing
        if(map.getValue(n) == Tile.EMPTY) {
            neighbours.add(1 to State(n, facing))
        }

        return neighbours
    }
}

class Day16: Solver {
    override fun solve(lines: List<String>, partTwo: Boolean): String {
        var start = Coordinate(-1, -1)
        var end = Coordinate(-1,-1)
        val map = mutableMapOf<Coordinate, Tile>()
        lines.forEachIndexed { y, row ->
            row.forEachIndexed { x, cell ->
                val c = Coordinate(x, y)
                when (cell) {
                    '#' -> map[c] = Tile.WALL
                    '.' -> map[c] = Tile.EMPTY
                    'S' -> {
                        start = c
                        map[c] = Tile.EMPTY
                    }
                    'E' -> {
                        end = c
                        map[c] = Tile.EMPTY
                    }
                }
            }
        }

        val goalStates = setOf(
            State(end, Direction.LEFT),
            State(end, Direction.UP),
            State(end, Direction.RIGHT),
            State(end, Direction.DOWN)
        )
        val isGoal = fun (s: State): Boolean = goalStates.contains(s)
        val startState = State(start, Direction.RIGHT)
        val h = fun (s: State): Int = abs(end.x - s.coordinate.x) + abs(end.y - s.coordinate.y)

        val result = aStar(startState, isGoal, h, map)
        val bestScore = result.sumOf { it.second }
        if(!partTwo) {
            return bestScore.toString()
        }
        println(bestScore)

        val r = searchAllPaths(startState, isGoal, h, map, bestScore)


        return r.size.toString()

    }

    private fun searchAllPaths(startState: State, isGoal: (State) -> Boolean, h: (State) -> Int, map: Map<Coordinate, Tile>, bestScore: Int): Set<Coordinate> {
        val path = mutableListOf<Pair<Int, State>>()
        val next = 0 to startState
        val res = mutableSetOf<Coordinate>()
        search(path, next, isGoal, h, map, bestScore, res)
        return res
    }

    private fun search(
        path: List<Pair<Int, State>>,
        next: Pair<Int, State>,
        isGoal: (State) -> Boolean,
        h: (State) -> Int,
        map: Map<Coordinate, Tile>,
        bestScore: Int,
        res: MutableSet<Coordinate>
    ) {
        for (n in next.second.getNeighbours(map)) {
            val r = aStar(n.second, isGoal, h, map)
            val score = r.sumOf { it.second }
            val pathWithNext = path.toMutableList().also { it.add(n) }
            val pathScoreSoFar = pathWithNext.sumOf { it.first }
            if((pathScoreSoFar + score) == bestScore) {
                res.add(n.second.coordinate)
                search(
                    pathWithNext,
                    n,
                    isGoal,
                    h,
                    map,
                    bestScore,
                    res
                )

            }
        }
    }

    private fun printSpots(spots: Set<Coordinate>, map: Map<Coordinate, Tile>) {
        val height = map.keys.maxOf { it.y }
        val width = map.keys.maxOf { it.x }
        var s = ""
        for (y in 0 .. height) {
            for (x in 0 .. width) {
                val c = Coordinate(x, y)
                val t = map.getValue(c)
                s += (when {
                    t == Tile.WALL -> "#"
                    spots.contains(c) -> "O"
                    else -> "."
                })
            }
            s += "\n"
        }
        println(s)
    }

    private fun printPath(result: List<Pair<State, Int>>, map: Map<Coordinate, Tile>) {
        val stateMap = result.map { it.first }.associateBy { it.coordinate }
        val height = map.keys.maxOf { it.y }
        val width = map.keys.maxOf { it.x }
        var s = ""
        for (y in 0 .. height) {
            for (x in 0 .. width) {
                val c = Coordinate(x, y)
                val t = map.getValue(c)
                if(t == Tile.WALL) {
                    s += "#"
                } else {
                    if(stateMap.containsKey(c)) {
                        val st = stateMap[c]!!
                        s += st.facing
                    } else {
                        s += "."
                    }
                }
            }
            s += "\n"
        }
        println(s)
    }

    private fun aStar(startState: State, isGoal: (State) -> Boolean, h:(State) -> Int, map: Map<Coordinate, Tile>): List<Pair<State, Int>> {
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

            for (n in curr.getNeighbours(map)) {
                val neighbour = n.second
                val tentativeGScore = gScore.getValue(curr) + n.first
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

    private fun reconstructPath(cameFrom: Map<State, State>, s: State): List<Pair<State, Int>> {
        val path = mutableListOf(s to 0)
        var curr = s
        while (cameFrom.containsKey(curr)) {
            val n = cameFrom[curr]!!
            val c = cost(curr, n)
            path.add(n to c)
            curr = n
        }
        return path.reversed()
    }

    private fun cost(s: State, n: State): Int {
        return if(s.facing == n.facing) 1 else 1000
    }
}