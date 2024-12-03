package se.horv

import se.horv.day01.Day01
import se.horv.day02.Day02
import java.io.File


fun main() {

    val filePath = "src/main/kotlin/day02/input.txt"
    val lines = File(filePath).readLines()
    val result = Day02().solve(lines, true)

    println(result)
}