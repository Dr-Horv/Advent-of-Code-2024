package se.horv.day15

import se.horv.util.Coordinate
import se.horv.util.Direction
import se.horv.util.Solver

enum class Tile {
    BOX,
    WALL,
    EMPTY
}

class Day15: Solver {
    override fun solve(lines: List<String>, partTwo: Boolean): String {
        var robot = Coordinate(-1,-1)
        val map = mutableMapOf<Coordinate, Tile>()
        val sequence = mutableListOf<Direction>()
        var y = 0
        for (line in lines) {
            if(line.startsWith("#")) {
                line.forEachIndexed { x, c ->
                    when (c) {
                        '#' -> map[Coordinate(x, y)] = Tile.WALL
                        'O' -> map[Coordinate(x, y)] = Tile.BOX
                        '@' -> {
                            robot = Coordinate(x, y)
                            map[Coordinate(x, y)] = Tile.EMPTY
                        }
                        '.' -> map[Coordinate(x, y)] = Tile.EMPTY
                    }
                }
            } else if (line.trim() == "") {
                continue
            } else {
                sequence.addAll(line.map {
                    when(it) {
                        '<' -> Direction.LEFT
                        '^' -> Direction.UP
                        'v'-> Direction.DOWN
                        '>' -> Direction.RIGHT
                        else -> throw IllegalArgumentException()
                    }
                })
            }
            y++
        }

        for (move in sequence) {
            robot = performMove(move, robot, map)
        }

        return calculateBoxGPSs(map).toString()


    }

    private fun printMap(robot: Coordinate, map: Map<Coordinate, Tile>) {
        val height = map.keys.maxOf { it.y }
        val width = map.keys.maxOf { it.x }
        var s = ""
        for (y in 0 .. height) {
            for (x in 0 .. width) {
                val coordinate = Coordinate(x, y)
                val c = map[coordinate] ?: throw IllegalArgumentException()
                s += if(coordinate == robot) {
                    "@"
                } else {
                    when (c) {
                        Tile.BOX -> "O"
                        Tile.WALL -> "#"
                        Tile.EMPTY -> "."
                    }
                }

            }
            s += "\n"
        }
        println(s)
    }

    private fun calculateBoxGPSs(map: Map<Coordinate, Tile>): Int =
        map.entries.filter { it.value == Tile.BOX }.sumOf { it.key.y * 100 + it.key.x }

    private fun attemptPush(to: Coordinate, from: Coordinate, map: MutableMap<Coordinate, Tile>) {
        if(map.getValue(from) == Tile.BOX && map.getValue(to) == Tile.EMPTY) {
            map[to] = Tile.BOX
            map[from] = Tile.EMPTY
        }
    }

    private fun performMove(move: Direction, robot: Coordinate, map: MutableMap<Coordinate, Tile>): Coordinate {
        var nextBreak = robot
        var next = robot
        while (true) {
            next += move
            if(map[next] == Tile.WALL || map[next] == Tile.EMPTY) {
                nextBreak = next
                break
            }
        }

        val moveOpposite = move.opposite()
        var prev = nextBreak
        var curr = nextBreak + moveOpposite
        while (curr != robot) {
            attemptPush(prev,curr, map)
            prev = curr
            curr += moveOpposite
        }

        val newPos = robot + move
        return if(map.getValue(newPos) == Tile.EMPTY) {
            newPos
        } else {
            robot
        }

    }
}