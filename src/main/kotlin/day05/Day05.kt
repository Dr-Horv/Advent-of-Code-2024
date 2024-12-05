package se.horv.day05

import se.horv.util.Solver

data class Rule(val left: Int, val right: Int)

class Day05: Solver {
    override fun solve(lines: List<String>, partTwo: Boolean): String {
        val rules = mutableSetOf<Rule>()
        val updates = mutableListOf<List<Int>>()
        for (line in lines) {
            if(line.contains("|")) {
                val parts = line.split("|").map { it.trim() }
                val r = Rule(parts[0].toInt(), parts[1].toInt())
                rules.add(r)
            }

            if(line.contains(",")) {
                val pages = line.split(",").map { it.toInt() }
                updates.add(pages)
            }
        }

        val validUpdates = mutableListOf<List<Int>>()
        val invalidUpdates = mutableListOf<List<Int>>()
        for (update in updates) {
            val cantPrint = mutableSetOf<Int>()
            var valid = true
            for (number in update) {
                if(cantPrint.contains(number)) {
                    valid = false
                    break
                }
                for (r in rules) {
                    if(r.right == number) {
                        cantPrint.add(r.left)
                    }
                }
            }
            if (valid) {
                validUpdates.add(update)
            } else {
                invalidUpdates.add(update)
            }
        }

        return if(!partTwo) {
            validUpdates
        } else {
            invalidUpdates.map {
                sortUpdate(it, rules)
            }
        }.sumOf { it[it.size / 2] }.toString()
    }

    private fun sortUpdate(
        it: List<Int>,
        rules: MutableSet<Rule>
    ) = it.sortedWith(java.util.Comparator { i1, i2 ->
        for (r in rules) {
            if (r.left == i1 && r.right == i2) {
                return@Comparator -1
            } else if (r.right == i1 && r.left == i2) {
                return@Comparator 1
            }
        }
        return@Comparator 0
    })
}