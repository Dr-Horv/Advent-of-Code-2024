package se.horv.day01

import se.horv.util.Solver
import kotlin.math.abs

class Day01:Solver {
    override fun solve(lines: List<String>, partTwo: Boolean): String {
        val lists = mutableListOf<MutableList<Int>>()
        lists.add(mutableListOf())
        lists.add(mutableListOf())

        for (line in lines) {
            val parts = line.split("\\s+".toRegex()).map { it.toInt() }
            parts.indices.forEach { i -> lists[i].add(parts[i]) }
        }

        val list1 = lists[0]
        val list2 = lists[1]

        list1.sort()
        list2.sort()

        if (!partTwo) {
            var dist = 0
            for (i in list1.indices){
                dist += abs(list1[i] - list2[i])
            }

            return dist.toString()
        }

        val map = mutableMapOf<Int, Int>().withDefault { 0 }
        for (n in list2) {
            map[n] = map.getValue(n) + 1
        }

        var simularity = 0
        for (i in list1) {
            simularity += i * map.getValue(i)
        }

        return simularity.toString()

    }
}