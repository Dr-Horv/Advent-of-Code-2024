package se.horv.day06
import se.horv.util.Coordinate
import se.horv.util.Direction
import se.horv.util.Solver

enum class Tile {
    FREE,
    OBSTACLE,
}

data class Guard (var position: Coordinate, var direction: Direction) {
    override fun toString(): String {
        return "$position $direction"
    }
}

data class Result (val visited: Set<Guard>, val loop: Boolean)

class Day06:Solver {

    override fun solve(lines: List<String>, partTwo: Boolean): String {
        val map = mutableMapOf<Coordinate, Tile>()
        var guard = Guard(Coordinate(0, 0), Direction.RIGHT)
        lines.forEachIndexed { y, row ->
            row.forEachIndexed { x, cell ->
                val coord = Coordinate(x=x, y=y)
                when (cell) {
                    '#' -> map[coord] = Tile.OBSTACLE
                    '.' -> map[coord] = Tile.FREE
                    '^' -> {
                        map[coord] = Tile.FREE
                        guard = Guard(coord, Direction.UP)
                    }
                }
            }
        }

        val visited = patrol(guard.copy().position, map).visited
        val uniqueLocations = visited.map { it.position }.toSet()
        return if (!partTwo) {
            uniqueLocations.count().toString()
        } else {
            uniqueLocations.count {
                if (it == guard.position) {
                    false
                } else {
                    val newMap = map.toMutableMap()
                    newMap[it] = Tile.OBSTACLE
                    patrol(guard.position, newMap).loop
                }
            }.toString()
        }

    }

    private fun patrol(
        start: Coordinate,
        map: Map<Coordinate, Tile>,
    ): Result {
        var guard = Guard(start, Direction.UP)
        val visited = mutableSetOf(guard)
        while (true) {
            val next = guard.position + guard.direction
            if (map.contains(next)) {
                val tile = map.getValue(next)
                guard = if (tile == Tile.OBSTACLE) {
                    guard.copy(direction = guard.direction.turnRight())
                } else {
                    guard.copy(position = next)
                }
                if(visited.contains(guard)) {
                    return Result(visited, loop = true)
                }
                visited.add(guard)
            } else {
                // Out of bounds
                return Result(visited, loop = false)
            }
        }
    }
}