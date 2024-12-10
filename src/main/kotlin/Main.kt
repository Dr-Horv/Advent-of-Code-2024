package se.horv

import se.horv.day01.Day01
import se.horv.day02.Day02
import se.horv.day03.Day03
import se.horv.day04.Day04
import se.horv.day05.Day05
import se.horv.day06.Day06
import se.horv.day07.Day07
import se.horv.day08.Day08
import se.horv.day09.Day09
import se.horv.day10.Day10
import se.horv.util.Solver
import java.io.File

enum class Day {
    Day01,
    Day02,
    Day03,
    Day04,
    Day05,
    Day06,
    Day07,
    Day08,
    Day09,
    Day10,
    Day11,
    Day12,
    Day13,
    Day14,
    Day15,
    Day16,
    Day17,
    Day18,
    Day19,
    Day20,
}

fun main(vararg args: String) {
    val day = Day.Day09
    val daySolver: Solver = when(day) {
        Day.Day01 -> Day01()
        Day.Day02 -> Day02()
        Day.Day03 -> Day03()
        Day.Day04 -> Day04()
        Day.Day05 -> Day05()
        Day.Day06 -> Day06()
        Day.Day07 -> Day07()
        Day.Day08 -> Day08()
        Day.Day09 -> Day09()
        Day.Day10 -> Day10()
        Day.Day11 -> TODO()
        Day.Day12 -> TODO()
        Day.Day13 -> TODO()
        Day.Day14 -> TODO()
        Day.Day15 -> TODO()
        Day.Day16 -> TODO()
        Day.Day17 -> TODO()
        Day.Day18 -> TODO()
        Day.Day19 -> TODO()
        Day.Day20 -> TODO()
    }
    val filePath = "src/main/kotlin/${day.toString().lowercase()}/input.txt"
    val lines = File(filePath).readLines()
    val result = daySolver.solve(lines, true)

    println(result)
}