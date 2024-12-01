package se.horv.util

interface Solver {
    fun solve(lines: List<String>, partTwo: Boolean = false): String
}