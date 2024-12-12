package se.horv.day12

import se.horv.util.Coordinate
import se.horv.util.Direction
import se.horv.util.Solver

data class Region(val id: Char, val plots: MutableSet<Coordinate> = mutableSetOf())

class Day12: Solver {
    override fun solve(lines: List<String>, partTwo: Boolean): String {
        val map = mutableMapOf<Coordinate, Char>()
        val candidates = mutableSetOf<Coordinate>()
        lines.forEachIndexed { y, r ->
            r.forEachIndexed { x, c ->
                val coord = Coordinate(x, y)
                map[coord] = c
                candidates.add(coord)
            }
        }

        val regions = mutableListOf<Region>()

        while (candidates.isNotEmpty()) {
            val next = candidates.first()
            regions.add(mapRegion(next, candidates, map))
        }

        return regions.sumOf {
            val area = it.plots.size
            area * if (!partTwo) calculatePerimeter(it, map) else calculateSides(it, map)
        }.toString()
    }

    private fun calculateSides(region: Region, map: Map<Coordinate, Char>): Int {
        val m = map.withDefault { '-' }
        val sideRegions = mutableMapOf<Direction, MutableList<Set<Coordinate>>>().withDefault { mutableListOf() }
        val sided = region.plots.filter { p ->
            Direction.entries.any { m.getValue(p + it) != region.id }
        }
        for (s in sided) {
            for (d in Direction.entries) {
                val n = s + d
                val c = m.getValue(n)
                if(c != region.id && !sideRegions.getValue(d).any { it.contains(n) }) {
                    val sidedRegion = mutableSetOf(n)
                    expandRegion(region.id, n, sidedRegion, m, d.turnRight(), d.opposite())
                    expandRegion(region.id, n, sidedRegion, m, d.turnLeft(), d.opposite())
                    val l = sideRegions.getValue(d)
                    l.add(sidedRegion)
                    sideRegions[d] = l
                }
            }
        }
        return sideRegions.map { it.value.size }.sum()
    }

    private fun expandRegion(
        id: Char,
        coordinate: Coordinate,
        region: MutableSet<Coordinate>,
        map: Map<Coordinate, Char>,
        directionTo: Direction,
        directionFrom: Direction
    ) {
        var curr = coordinate
        while (true) {
            val next = curr + directionTo
            if(map.getValue(next+directionFrom) != id || map.getValue(next) == id) {
                break
            }
            region.add(next)
            curr = next
        }
    }

    private fun calculatePerimeter(region: Region, map: Map<Coordinate, Char>): Int {
        val m = map.withDefault { '-' }
        return region.plots.sumOf { plot ->
            Direction.entries.map { dir ->
                val neighbour = plot + dir
                val c = m.getValue(neighbour)
                if (c != region.id) {
                    1
                } else {
                    0
                }
            }.sum()
        }
    }

    private fun mapRegion(start: Coordinate, candidates: MutableSet<Coordinate>, map: Map<Coordinate, Char>): Region {
        val v = map.getValue(start)
        val r = Region(v, mutableSetOf(start))
        candidates.remove(start)
        explore(start, r, candidates, map)
        return r
    }

    private fun explore(
        coord: Coordinate,
        region: Region,
        candidates: MutableSet<Coordinate>,
        map: Map<Coordinate, Char>
    ) {
        for (it in Direction.entries) {
            val next = coord + it
            if(region.plots.contains(next)) {
                continue
            }

            if(map.containsKey(next)) {
                val v = map.getValue(next)
                if(v == region.id) {
                    candidates.remove(next)
                    region.plots.add(next)
                    explore(next, region, candidates, map)

                }
            }
        }
    }
}