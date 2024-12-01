package se.horv

import se.horv.day01.Day01
import java.io.File


fun main() {

    val filePath = "src/main/kotlin/day01/input.txt"
    val lines = File(filePath).readLines()
    val result = Day01().solve(lines, true)

    println(result)
}