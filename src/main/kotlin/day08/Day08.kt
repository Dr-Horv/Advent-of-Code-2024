package se.horv.day08

import se.horv.util.Coordinate
import se.horv.util.Solver

class Day08: Solver {
    override fun solve(lines: List<String>, partTwo: Boolean): String {
        val maxY = lines.lastIndex
        val maxX = lines.first().lastIndex
        val antennas = mutableMapOf<Char, MutableList<Coordinate>>().withDefault { mutableListOf() }
        lines.forEachIndexed { y, row ->
            row.forEachIndexed { x, c ->
                if (c != '.') {
                    val l = antennas.getValue(c)
                    l.add(Coordinate(x, y))
                    antennas[c] = l
                }
            }
        }

        val withinBounds = fun (c: Coordinate): Boolean = c.x in 0.. maxX && c.y in 0.. maxY

        val antinodes = mutableSetOf<Coordinate>()
        for (entry in antennas.entries) {
            val nodes = entry.value
            for (i in nodes.indices) {
                for (j in (i+1) .. nodes.lastIndex) {
                    val n1 = nodes[i]
                    val n2 = nodes[j]
                    val diff = n2 - n1
                    if(!partTwo) {
                        antinodes.addAll(
                            listOf(
                                n1 + diff,
                                n1 - diff,
                                n2 + diff,
                                n2 - diff
                            ).filter {
                                it != n1 && it != n2 && withinBounds(it)
                            }
                        )
                    } else {
                        antinodes.add(n1)
                        antinodes.add(n2)
                        var next = n1 + diff
                        while (withinBounds(next)) {
                            antinodes.add(next)
                            next += diff
                        }

                        var next2 = n1 - diff
                        while (withinBounds(next2)) {
                            antinodes.add(next2)
                            next2 -= diff
                        }

                    }

                }
            }
        }

        return antinodes.size.toString()
    }
}