package se.horv.day19

import se.horv.util.Solver

private val CACHE = mutableMapOf<String, Long>()

class Day19 : Solver {
    override fun solve(lines: List<String>, partTwo: Boolean): String {
        val it = lines.iterator()
        val towels = it.next().split(",").map { it.trim() }.toMutableList()
        val arrangements = mutableListOf<String>()
        it.next()
        while (it.hasNext()) {
            arrangements.add(it.next())
        }

        towels.sortByDescending { it.length }
        return if (!partTwo) {
            arrangements.filter { isPossible(it, towels) > 0 }.size.toString()
        } else {
            arrangements.sumOf { isPossible(it, towels) }.toString()
        }
    }

    private fun isPossible(arrangement: String, towels: List<String>): Long {
        if (CACHE.containsKey(arrangement)) return CACHE.getValue(arrangement)
        if (arrangement == "") return 1
        val r = towels.sumOf {
            when {
                arrangement.startsWith(it) -> isPossible(arrangement.substring(it.length), towels)
                else -> 0
            }
        }
        CACHE[arrangement] = r
        return r
    }
}