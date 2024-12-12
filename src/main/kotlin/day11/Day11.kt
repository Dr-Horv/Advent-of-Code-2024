package se.horv.day11

import se.horv.util.Solver

data class StoneNode(var number: Long, var left: StoneNode? = null, var right: StoneNode? = null) {
    fun addAfter(n: StoneNode) {
        val meRight = right
        right = n
        n.left = this
        n.right = meRight
        meRight?.left = n
    }
}

class Day11: Solver {
    private val cache = mutableMapOf<Pair<Long, Int>, Long>()

    override fun solve(lines: List<String>, partTwo: Boolean): String {
        return lines.first()
            .split(" ")
            .map { it.toLong() }
            .sumOf {
                blink(it, if (!partTwo) 25 else 75)
            }
            .toString()
    }

    private fun blink(number: Long, blinks: Int): Long {
        if(blinks == 0) {
            return 1
        }
        if(cache.containsKey(number to blinks)) {
            return cache[number to blinks]!!
        }

        if(number == 0L) {
            val v =  blink(1L, blinks-1)
            cache[number to blinks] = v
            return v
        } else {
            val s = number.toString()
            if(s.length % 2 == 0) {
                val mi = s.length / 2;
                val ln = s.substring(0, mi).toLong()
                val rn = s.substring(mi).toLong()
                val v = blink(ln, blinks-1) + blink(rn, blinks-1)
                cache[number to blinks] = v
                return v
            } else {
                val v = blink(number * 2024, blinks-1)
                cache[number to blinks] = v
                return v
            }
        }
    }

}