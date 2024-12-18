package se.horv.day17

import kotlinx.coroutines.*
import se.horv.util.Solver
import kotlin.math.pow

enum class OpCode {
    adv,
    bxl,
    bst,
    jnz,
    bxc,
    out,
    bdv,
    cdv;


    companion object {
        fun from(s: String): OpCode = when(s) {
            "0" -> adv
            "1" -> bxl
            "2" -> bst
            "3" -> jnz
            "4" -> bxc
            "5" -> out
            "6" -> bdv
            "7" -> cdv
            else -> throw IllegalArgumentException("Unknown opcode $s")
        }
        fun from(s: Int): OpCode = when(s) {
            0 -> adv
            1 -> bxl
            2 -> bst
            3 -> jnz
            4 -> bxc
            5 -> out
            6 -> bdv
            7 -> cdv
            else -> throw IllegalArgumentException("Unknown opcode $s")
        }
    }
}


data class Computer(var a: Int, var b: Int, var c: Int) {
    private val output = mutableListOf<Int>()
    private var pointer = 0
    private fun comboOperand(operand: Int): Int = when (operand) {
        in 0..3 -> operand
        4 -> a
        5 -> b
        6 -> c
        else -> {throw IllegalArgumentException("Invalid operand $operand")}
    }
    fun runProgram(program: List<Int>): String {
        output.clear()
        pointer = 0

        while (pointer < program.lastIndex) {
            val opCode = OpCode.from(program[pointer])
            val operand = program[pointer + 1]
            when (opCode) {
                OpCode.adv -> {
                    val v = comboOperand(operand)
                    val denominator = 2.0.pow(v)
                    a = (a/denominator).toInt()
                    pointer += 2
                }
                OpCode.bxl -> {
                    b = b xor operand
                    pointer += 2
                }
                OpCode.bst -> {
                    b = comboOperand(operand) % 8
                    pointer += 2
                }
                OpCode.jnz -> {
                    if(a != 0) {
                        pointer = operand
                    } else {
                        pointer += 2
                    }
                }
                OpCode.bxc -> {
                    b = b xor c
                    pointer += 2
                }
                OpCode.out -> {
                    output.add(comboOperand(operand) % 8)
                    pointer += 2
                }
                OpCode.bdv -> {
                    val v = comboOperand(operand)
                    val denominator = 2.0.pow(v)
                    b = (a/denominator).toInt()
                    pointer += 2
                }
                OpCode.cdv -> {
                    val v = comboOperand(operand)
                    val denominator = 2.0.pow(v)
                    c = (a/denominator).toInt()
                    pointer += 2
                }
            }

        }

        return output.joinToString(",")
    }
}


@OptIn(ExperimentalCoroutinesApi::class)
class Day17: Solver {
    override fun solve(lines: List<String>, partTwo: Boolean): String {
        val it = lines.iterator()
        val registerA = it.next().removePrefix("Register A: ").toInt()
        val registerB = it.next().removePrefix("Register B: ").toInt()
        val registerC = it.next().removePrefix("Register C: ").toInt()
        it.next()
        val programS = it.next().removePrefix("Program: ")
        val program = programS.split(",").map { it.toInt() }

        if(!partTwo) {
            val computer = Computer(registerA, registerB, registerC)
            return computer.runProgram(program)
        } else {
            for (a in 0..8) {
                val r = recurse(program, program.lastIndex, a.toLong(), program.size == 6)
                if(r != null) {
                    return r.toString()
                }
            }
        }

        throw IllegalArgumentException("Invalid input")
    }

    private fun recurse(program: List<Int>, index: Int, soFar: Long, isExample: Boolean): Long? {
        for (a in 0..8) {
            val nextA = soFar * 8 + a

            val r = if(isExample) exampleProgram(nextA) else  myProgram(nextA)
            val cmp = program.drop(index).joinToString(",")
            if(r == cmp) {
                if(index == 0) {
                    return nextA
                }
                val r2 = recurse(program, index-1, nextA, program.size == 6)
                if(r2 != null) {
                    return r2
                }
            }
        }
        return null
    }

    private fun myProgram(i: Long): String {
        val output = mutableListOf<Long>()
        var a: Long = i

        // 2,4,1,3,7,5,0,3,4,1,1,5,5,5,3,0
        var b = 0L
        var c = 0L

        while (true) {
            // 2,4 - bst
            // 1,3 - bxl
            // b XOR 011
            b = (a % 8L) xor 3L

            // 7,5 - cdv
            c = (a / 2.0.pow(b.toInt())).toLong()

            // 0,3 - adv
            a = a / 8

            // 4,1 - bxc
            b = b xor c

            // 1,5 - bxl
            b = b xor 5

            // 5,5 - out
            output.add(b % 8)

            // 3,0 - jnz
            if(a == 0L) {
                break
            }
        }


        return output.joinToString(",")
    }

    // Example
    private fun exampleProgram(i: Long): String {
        val output = mutableListOf<Long>()
        // 0,3,5,4,3,0

        // 0,3 - adv 3
        // A/2^3 = A/3
        var a = i
        while (true) {
            a = a/8L

            // 5,4 - out
            // a % 8
            output.add(a%8)

            // 3,0 - jnz

            if(a == 0L) {
                break
            }
        }


        return output.joinToString(",")
    }
}