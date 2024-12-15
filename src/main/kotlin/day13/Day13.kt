package se.horv.day13

import se.horv.util.Solver

data class Coordinate(val x: Long, val y: Long) {
    override fun toString(): String = "($x, $y)"
    operator fun plus(other: Coordinate): Coordinate = Coordinate(x + other.x, y + other.y)
    operator fun minus(other: Coordinate): Coordinate = Coordinate(x - other.x, y - other.y)
}

data class ClawMachine(val a: Coordinate, val b: Coordinate, val prize: Coordinate)
data class RatNum(val nominator: Long, val denominator: Long)
data class Expression (val real: RatNum, val variable: RatNum)

class Day13: Solver {
    override fun solve(lines: List<String>, partTwo: Boolean): String {
        val claws = mutableListOf<ClawMachine>()
        val it = lines.iterator()
        while (it.hasNext()) {
            val n = it.next()
            if(n == "") {
                continue
            }
            val a = parseCoordinate(n, "Button A:")
            val b = parseCoordinate(it.next(), "Button B:")
            val prize = parsePrizeCoordinate(it.next()) + if (!partTwo) Coordinate(0,0) else Coordinate(10000000000000, 10000000000000)
            claws.add(ClawMachine(a, b, prize))

        }

        var tokens = 0L
        for (c in claws) {
            tokens += trySolve(c)
        }
        return tokens.toString()
    }

    private fun trySolve(clawMachine: ClawMachine): Long {
        val x1 = clawMachine.a.x
        val x2 = clawMachine.b.x
        val y1 = clawMachine.a.y
        val y2 = clawMachine.b.y
        val xValue = clawMachine.prize.x
        val yValue = clawMachine.prize.y
        val aExpression1 = Expression(RatNum(xValue, x1), RatNum(-x2, x1))
        val aExpression2 = Expression(RatNum(yValue, y1), RatNum(-y2, y1))

        val b1 = RatNum(aExpression1.variable.nominator * -1, aExpression1.variable.denominator)
        val b2 = RatNum(aExpression2.variable.nominator, aExpression2.variable.denominator)
        val real1 = RatNum(aExpression1.real.nominator, aExpression1.real.denominator)
        val real2 = RatNum(aExpression2.real.nominator * -1, aExpression2.real.denominator)

        val b1Normal = b1.nominator * b2.denominator
        val b2Normal = b2.nominator * b1.denominator

        val real1Normal = real1.nominator * real2.denominator
        val real2Normal = real2.nominator * real1.denominator

        val bReal = (real1Normal + real2Normal)
        val bVariable = (b1Normal+b2Normal)
        if(bReal % bVariable == 0L) {
            val b = bReal/bVariable

            val aBefore = (aExpression2.real.nominator + aExpression2.variable.nominator * b)
            val divide = aExpression2.real.denominator
            if(aBefore % divide == 0L) {
                val a = aBefore / divide
                return 3 * a + b
            }
        }

        return 0
    }

    private fun parsePrizeCoordinate(next: String): Coordinate {
        val r = Regex(".*X=(?<x>\\d+), Y=(?<y>\\d+)").find(next)!!
        val x = r.groups["x"]!!.value.toLong()
        val y = r.groups["y"]!!.value.toLong()
        return Coordinate(x, y)
    }

    private fun parseCoordinate(line: String, prefix: String): Coordinate {
        val parts = line.split(prefix)
        val part = parts[1].trim()
        val r = Regex("X\\+(?<x>\\d+), Y\\+(?<y>\\d+)").find(part)!!
        val x = r.groups["x"]!!.value.toLong()
        val y = r.groups["y"]!!.value.toLong()
        return Coordinate(x,y)
    }
}