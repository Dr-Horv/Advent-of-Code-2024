package se.horv.day25

import se.horv.util.Solver

class Day25: Solver {
    override fun solve(lines: List<String>, partTwo: Boolean): String {
        val columns = lines.first().length
        val locksAndKeyInput = mutableListOf<String>()
        var curr = ""
        for (line in lines) {
            if(line.isBlank()) {
                locksAndKeyInput.add(curr)
                curr = ""
            } else {
                curr += "$line\n"
            }
        }
        locksAndKeyInput.add(curr)


        val locks = mutableListOf<List<Int>>()
        val keys = mutableListOf<List<Int>>()

        val allSquares = "#".repeat(columns)

        for (keyOrLock in locksAndKeyInput) {
            val inputLines = keyOrLock.split("\n").filter { it.isNotBlank() }
            val first = inputLines.first()
            val parsed = parse(inputLines.subList(1, inputLines.size-1))
            if(first == allSquares) {
                locks += parsed
            } else {
                keys += parsed
            }
        }

        val height = lines.takeWhile { it.isNotEmpty() }.size - 2

        var fits = 0
        for (lock in locks) {
            for (key in keys) {
                var test = true
                for(i in key.indices) {
                    test = test && (lock[i] + key[i] <= height)
                }
                if (test) {
                    fits++
                }
            }
        }

        return fits.toString()
    }

    private fun parse(lines: List<String>): List<Int> {
        val curr = mutableListOf<Int>()
        val cols = lines.first().length
        for (i in 0 until cols) {
            curr.add(0)
        }
        for (line in lines) {
            for((xi, c) in line.withIndex()) {
                val dxi = if(c == '#') 1 else 0
                curr[xi] = curr[xi] + dxi
            }
        }

        return curr
    }
}