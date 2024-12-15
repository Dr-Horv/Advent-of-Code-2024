package se.horv.day14

import se.horv.util.Coordinate
import se.horv.util.Solver

data class Robot(var p: Coordinate, val v: Coordinate)

class Day14: Solver {
    override fun solve(lines: List<String>, partTwo: Boolean): String {
        val robots = mutableListOf<Robot>()
        for (line in lines) {
            val parts = line.split(" ")
            val ps = parts[0].split(",")
            val vs = parts[1].split(",")
            val p = Coordinate(ps[0].substring(2).toInt(), ps[1].toInt())
            val v = Coordinate(vs[0].substring(2).toInt(), vs[1].toInt())
            robots.add(Robot(p, v))
        }
        val height = robots.maxOf { it.p.y }
        val width = robots.maxOf { it.p.x }

        if(!partTwo) {
            for (robot in robots) {
                forward(robot, 100, height, width)
            }

            return calculateSafetyFactor(robots, height, width).toString()
        } else {
            var i = 1
            while (true) {
                robots.forEach { forward(it, 1, height, width) }
                val robotPos = robots.associateBy { it.p }
                val test = robotPos.entries.any {
                    for (dx in 0..10) {
                        val c = it.key + Coordinate(dx,0)
                        if(!robotPos.contains(c)) {
                            return@any false
                        }
                    }
                    return@any true
                }
                if(test) {
                    println(i)
                    printRobots(robots, height, width)
                    return i.toString()
                }
                i++
            }
        }
    }

    private fun printRobots(robots: List<Robot>, height: Int, width: Int) {
        val robotPos = robots.associateBy { it.p }
        var s = ""
        for (y in 0 .. height) {
            for (x in 0 .. width) {
                s += if(robotPos.contains(Coordinate(x,y))) {
                    "#"
                } else {
                    "."
                }
            }
            s += "\n"
        }
        println(s)
    }

    private fun calculateSafetyFactor(robots: List<Robot>, height: Int, width: Int): Int {
        val middleX = width / 2
        val middleY = height / 2
        return robots
            .filter { it.p.x != middleX && it.p.y != middleY }
            .groupBy {
                val p = it.p
                if(p.x < middleX) {
                    if(p.y < middleY) {
                        1
                    } else {
                        3
                    }
                } else {
                    if(p.y < middleY) {
                        2
                    } else {
                        4
                    }
                }
        }.values.map { it.size }
            .fold(1, Int::times)
    }

    private fun forward(robot: Robot, seconds: Int, height: Int, width: Int) {
        val h = height + 1
        val w = width + 1
        val newX = (robot.p.x + robot.v.x * seconds + w*seconds) % w
        val newY = (robot.p.y + robot.v.y * seconds + h*seconds) % h
        robot.p = Coordinate(newX, newY)
    }
}