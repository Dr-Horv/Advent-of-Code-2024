package se.horv.util

enum class Direction {
    UP, DOWN, LEFT, RIGHT;

    override fun toString(): String = when(this) {
        UP -> "^"
        DOWN -> "v"
        LEFT -> "<"
        RIGHT -> ">"
    }

    fun turnRight(): Direction = when (this) {
        UP -> RIGHT
        DOWN -> LEFT
        LEFT -> UP
        RIGHT -> DOWN
    }

    fun turnLeft(): Direction = when (this) {
        UP -> LEFT
        DOWN -> RIGHT
        LEFT -> DOWN
        RIGHT -> UP
    }

    fun opposite(): Direction = when (this) {
        UP -> DOWN
        DOWN -> UP
        LEFT -> RIGHT
        RIGHT -> LEFT
    }

    companion object {
        fun fromString(s: String): Direction = when (s) {
            "<" -> LEFT
            ">" -> RIGHT
            "v"-> DOWN
            "^" -> UP
            else -> throw IllegalArgumentException("Invalid string $s")
        }

        fun fromChar(c: Char): Direction = when (c) {
            '<' -> LEFT
            '>' -> RIGHT
            'v' -> DOWN
            '^' -> UP
            else -> throw IllegalArgumentException("Invalid character $c")
        }

    }
}