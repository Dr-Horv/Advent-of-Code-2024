package se.horv.day15

import se.horv.util.Coordinate
import se.horv.util.Direction
import se.horv.util.Solver

enum class Tile {
    WALL,
    EMPTY
}

data class Box(val id: Int, var coordinates: Set<Coordinate>) {
    companion object {
        var idCounter = 0
    }
    constructor(coordinates: Set<Coordinate>) : this(idCounter++, coordinates)

    fun move(dir: Direction): Box = Box(id, coordinates.map { it + dir }.toSet())
}

class Day15: Solver {
    override fun solve(lines: List<String>, partTwo: Boolean): String {
        var robot = Coordinate(-1,-1)
        val map = mutableMapOf<Coordinate, Tile>()
        val boxes = mutableSetOf<Box>()
        val sequence = mutableListOf<Direction>()
        var y = 0
        for (line in lines) {
            if(line.startsWith("#")) {
                var x = 0
                line.forEach { c ->
                    when (c) {
                        '#' -> addTile(x,y, Tile.WALL, map, partTwo)
                        'O' -> {
                            boxes.add(
                                Box(
                                    if(!partTwo) mutableSetOf(Coordinate(x,y))
                                    else mutableSetOf(Coordinate(x,y), Coordinate(x+1,y))
                                )
                            )
                            addTile(x, y, Tile.EMPTY, map, partTwo)
                        }
                        '@' -> {
                            robot = Coordinate(x, y)
                            addTile(x, y, Tile.EMPTY, map, partTwo)
                        }
                        '.' -> addTile(x, y, Tile.EMPTY, map, partTwo)
                    }
                    if(!partTwo) {
                        x++
                    } else {
                        x+=2
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
            robot = performMove(move, robot, boxes, map)
        }

        return calculateBoxGPSs(boxes).toString()

    }

    private fun addTile(
        x: Int,
        y: Int,
        tile: Tile,
        map: MutableMap<Coordinate, Tile>,
        partTwo: Boolean
    ) {
        map[Coordinate(x, y)] = tile
        if (partTwo) {
            map[Coordinate(x + 1, y)] = tile
        }
    }


    private fun performMove(move: Direction, robot: Coordinate, boxes: MutableSet<Box>, map: Map<Coordinate, Tile>): Coordinate {
        val next = robot + move
        val tile = map.getValue(next)
        if(tile == Tile.WALL) {
            return robot
        } else {
            val boxesToRemove = mutableSetOf<Box>()
            val boxesToAdd = mutableSetOf<Box>()
            val toMove = boxes.filter { it.coordinates.contains(next) }.toMutableSet()
            while (toMove.isNotEmpty()) {
                val n = toMove.first()
                toMove.remove(n)
                val nx = n.move(move)
                if(nx.coordinates.all { map.getValue(it) == Tile.EMPTY }) {
                    toMove.addAll(
                        boxes.filter { it.id != nx.id && it.coordinates.any { c -> nx.coordinates.contains(c) } }
                    )
                    boxesToAdd.add(nx)
                    boxesToRemove.add(n)
                } else {
                    return robot
                }
            }
            boxes.removeAll(boxesToRemove)
            boxes.addAll(boxesToAdd)
            return next
        }
    }

    private fun calculateBoxGPSs(boxes: Set<Box>): Int =
        boxes.map { b -> b.coordinates.minBy { c -> c.x } }.sumOf { it.y * 100 + it.x }


    private fun printMap(robot: Coordinate, boxes: Set<Box>, map: Map<Coordinate, Tile>) {
        val height = map.keys.maxOf { it.y }
        val width = map.keys.maxOf { it.x }
        var s = ""
        for (y in 0 .. height) {
            for (x in 0..width) {
                val coordinate = Coordinate(x, y)
                val c = map[coordinate] ?: throw IllegalArgumentException()
                if(robot == coordinate) {
                    s += "@"
                } else if (c == Tile.WALL) {
                    s += "#"
                } else if (c === Tile.EMPTY) {
                    val b = boxes.find { it.coordinates.contains(coordinate) }
                    s += if (b != null) {
                        if(b.coordinates.minBy { it.x } != coordinate) {
                            "]"
                        } else {
                            "["
                        }
                    } else {
                        "."
                    }
                }
            }
            s += "\n"
        }
        println(s)
    }

}