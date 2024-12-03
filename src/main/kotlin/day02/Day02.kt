package se.horv.day02

import se.horv.util.Solver

class Day02:Solver {
    private fun check(report: List<Int>): Boolean {
        return if(report.first() < report.last()) {
            checkAscending(report)
        } else if (report.first() > report.last()) {
            checkDescending(report)
        } else {
            false;
        }
    }

    private fun checkDescending(report: List<Int>): Boolean {
        for (i in 0..<report.lastIndex) {
            val diff = report[i] - report[i + 1]
            if (diff !in 1..3) {
                return false
            }
        }
        return true
    }

    private fun checkAscending(report: List<Int>): Boolean {
        for (i in 0..<report.lastIndex) {
            val diff = report[i + 1] - report[i]
            if (diff !in 1..3) {
                return false
            }
        }
        return true
    }

    override fun solve(lines: List<String>, partTwo: Boolean): String {
        val reports = lines.map { l -> l.split(" ").map { it.toInt() } }
        return reports.count {
            if(!partTwo) {
                check(it)
            } else {
                for(i in -1..reports.lastIndex) {
                    if(check(it.filterIndexed { index, _ -> index != i })) {
                        return@count true
                    }
                }
                return@count false
            }
        }.toString()


    }
}