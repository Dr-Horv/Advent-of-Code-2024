package se.horv.util

import kotlin.math.abs

data class Coordinate(val x: Int, val y: Int) {
    override fun toString(): String = "($x, $y)"
    operator fun plus(other: Coordinate): Coordinate = Coordinate(x + other.x, y + other.y)
    operator fun plus(dir: Direction): Coordinate = this.plus(
        when (dir) {
            Direction.UP -> Coordinate(0, -1)
            Direction.DOWN -> Coordinate(0, 1)
            Direction.LEFT -> Coordinate(-1, 0)
            Direction.RIGHT -> Coordinate(1, 0)
        }
    )
    operator fun minus(other: Coordinate): Coordinate = Coordinate(x - other.x, y - other.y)

    fun manhattanDistance(other: Coordinate): Int = abs(x - other.x) + abs(y - other.y)
    fun difference(other: Coordinate): Pair<Int, Int> = Pair(other.x - x, other.y - y)
}