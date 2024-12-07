package se.horv.day07

import se.horv.util.Solver

data class Equation(val target: Long, val numbers: List<Int>)

class Day07: Solver {
    override fun solve(lines: List<String>, partTwo: Boolean): String {
        val equations = mutableListOf<Equation>()
        for (line in lines) {
            val parts = line.split(":")
            val target = parts[0].toLong()
            val numbers = parts[1].trim().split(" ").map { it.toInt() }
            equations.add(Equation(target, numbers))
        }

        return equations.filter {
            canBeTrue(it.target, it.numbers.drop(1), it.numbers.first().toLong(), partTwo)
        }.sumOf { it.target }.toString()
    }

    private fun canBeTrue(target: Long, numbers: List<Int>, curr: Long, partTwo: Boolean): Boolean {
        if(curr == target && numbers.isEmpty()) return true
        if(curr > target) return false
        if(numbers.isEmpty()) return false

        val next = numbers.first()
        val rest = numbers.drop(1)
        return canBeTrue(target, rest, curr + next, partTwo) ||
                canBeTrue(target, rest, curr * next, partTwo) ||
                canBeTrue(target, rest, "$curr$next".toLong(), partTwo)

    }
}