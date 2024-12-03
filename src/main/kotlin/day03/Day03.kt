package se.horv.day03

import se.horv.util.Solver
import java.util.LinkedList

class Day03 : Solver {
    private val regex = Regex("""mul\((\d{1,3}),(\d{1,3})\)""")
    private fun processLine(line: String): Sequence<Int> =
        regex.findAll(line).map { m -> m.groups[1]!!.value.toInt() * m.groups[2]!!.value.toInt() }

    override fun solve(lines: List<String>, partTwo: Boolean): String {

        if(!partTwo) {
            return lines.flatMap { l -> processLine(l) }.sum().toString()
        }

        var enabled = true
        var result = ""
        var chars = LinkedList<Char>()
        for (line in lines) {
            for (char in line) {
                chars.addLast(char)
                if(enabled) {
                    result += char
                }
                // don't()
                if(enabled && chars.size == 7) {
                    if(chars == "don't()".toCharArray().toList()) {
                        enabled = false
                        chars = LinkedList()
                    } else {
                        chars.removeFirst()
                    }
                }

                // do()
                if(!enabled && chars.size == 4) {
                    if(chars == "do()".toCharArray().toList()) {
                        enabled = true
                        chars = LinkedList()
                    } else {
                        chars.removeFirst()
                    }
                }
            }
        }
        return processLine(result).sum().toString()

    }
}