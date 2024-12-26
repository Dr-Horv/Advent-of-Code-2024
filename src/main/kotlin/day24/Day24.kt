package se.horv.day24

import se.horv.util.Solver

sealed class Component(var outputName: String) {
    abstract fun getOutputValue(circuit: Map<String, Boolean?>): Boolean?
}

abstract class BinaryGate(outputName: String, val inputs: Set<String> = mutableSetOf()): Component(outputName)

class AndGate(outputName: String, inputs: Set<String> = mutableSetOf()): BinaryGate(outputName, inputs) {
    override fun getOutputValue(circuit: Map<String, Boolean?>): Boolean? {
        val values = inputs.map { circuit[it] }
        if(values.contains(null)) return null
        return values.all { it!! }
    }
}

class OrGate(outputName: String, inputs: Set<String> = mutableSetOf()): BinaryGate(outputName, inputs) {
    override fun getOutputValue(circuit: Map<String, Boolean?>): Boolean? {
        val values = inputs.map { circuit[it] }
        if(values.contains(null)) return null
        return values.any { it!! }
    }
}

class XorGate(outputName: String, inputs: Set<String> = mutableSetOf()): BinaryGate(outputName, inputs) {
    override fun getOutputValue(circuit: Map<String, Boolean?>): Boolean? {
        val values = inputs.map { circuit[it] }
        if(values.contains(null)) return null
        return values[0] != values[1]
    }
}

class SignalWire(outputName: String, var value: Boolean): Component(outputName) {
    override fun getOutputValue(circuit: Map<String, Boolean?>): Boolean {
        return value
    }
}

class Day24: Solver {
    override fun solve(lines: List<String>, partTwo: Boolean): String {
        val components = mutableMapOf<String, Component>()
        val signalWires = mutableMapOf<String, SignalWire>()
        for (line in lines) {
            if(line.contains(":")) {
                val parts = line.split(":").map { it.trim() }
                val name = parts[0]
                val signalWire = SignalWire(name, parts[1] == "1")
                components[name] = signalWire
                signalWires[name] = signalWire

            } else if (line.trim().isEmpty()) {
                continue
            } else if (line.contains("->")) {
                val parts = line.split("->").map { it.trim() }
                val lhs = parts[0]
                val rhs = parts[1]
                val lhsParts = lhs.split(" ")
                val inputs = setOf(lhsParts[0], lhsParts[2])
                when(lhsParts[1]) {
                    "XOR" -> components[rhs] = XorGate(rhs, inputs)
                    "AND" -> components[rhs] = AndGate(rhs, inputs)
                    "OR" -> components[rhs] = OrGate(rhs, inputs)
                }
            }
        }

        return if(!partTwo) {
            part1(components).toLong(2).toString()
        } else {
            verifyIntegrityPerBit(components)
                .sorted()
                .joinToString(",")
        }

    }

    private fun verifyIntegrityPerBit(components: Map<String, Component>): MutableSet<String> {
        val zs = components.keys.filter { it.startsWith("z") }.sorted()
        val culprits = mutableSetOf<String>()
        for ((i,n) in zs.withIndex()) {
            if(i == 0 || i == 1 || i == 45) {
                continue
            }
            val o = verifyIntegrity(setOf(n), components, 0)
            culprits.addAll(o)
        }

        return culprits
    }

    private fun verifyIntegrity(curr: Set<String>, components: Map<String, Component>, level: Int): Set<String> {
        if(level == 0) {
            val c = components[curr.first()]!!
            return if(c is XorGate) {
                verifyIntegrity(c.inputs, components, level + 1)
            } else {
                curr
            }
        } else if (level == 1) {
            val cs = curr.map { components[it]!! }
            val xorGs = cs.filterIsInstance<XorGate>()
            val orGs = cs.filterIsInstance<OrGate>()
            if(xorGs.size != 1 || orGs.size != 1) {
                val unexpectedGates =  cs.filter {
                    it is AndGate || it is SignalWire
                }.map { it.outputName }.toSet()
                if(unexpectedGates.isNotEmpty()) {
                    return unexpectedGates
                }

                if(xorGs.size > 1) {
                    val x1 = xorGs.first()
                    val x1in = x1.inputs.filter { it.startsWith("x") || it.startsWith("y") }
                    return if(x1in.isEmpty()) {
                        setOf(x1.outputName)
                    } else {
                        setOf(xorGs[1].outputName)
                    }
                }

            }

            return if(xorGs.size == 1 && orGs.size == 1) {
                val xorGInputs = xorGs.first().inputs.map { components[it]!! }
                val signal1 = xorGInputs.filterIsInstance<SignalWire>()

                val orGsInputs = orGs.first().inputs.map { components[it]!! }
                val andGs2 = orGsInputs.filterIsInstance<AndGate>()

                if(signal1.size != 2) {
                    val d = setOf(xorGs.first().outputName)
                    return d
                }

                if(andGs2.size != 2) {
                    val dd = orGsInputs.first { !andGs2.contains(it) }
                    return setOf(dd.outputName)
                }

                return emptySet()
            } else {
                curr
            }
        }

        return emptySet()
    }

    private fun prettyPrint(components: Map<String, Component>) {
        val zs = components.keys.filter { it.startsWith("z") }.sorted()
        var s = ""
        var i = 0
        for(n in zs) {
            println("----- $n --------")
            s = expandPrettyPrint(0, n, components, 1)
            println(s)
            i++

        }

    }


    private fun expandPrettyPrint(
        i: Int,
        n: String,
        components: Map<String, Component>,
        level: Int
    ): String {
        if(level == 4) {
            return ""
        }
        val pad = " ".repeat(level)
        var s1 = pad
        s1 += "in$i\n$pad"
        val c = components[n]!!
        s1 += when (c) {
            is AndGate -> "AND"
            is XorGate -> "XOR"
            is OrGate -> " OR"
            is SignalWire -> "value"//""  S " + if (c.value) '1' else '0'
            else -> throw IllegalArgumentException("Invalid gate $c")
        }
        s1 += "\n"

        if (c is BinaryGate) {
            /*
            for (input in c.inputs) {
                s1 += pad + pad + input + "\n"
            }
            s1 += "\n"

             */
            for ((id, input) in c.inputs.sortedWith(fun (n1, n2): Int {
                val ic1 = components[n1]!!
                val ic2 = components[n2]!!
                return compareComponentsByType(ic1, ic2)
            }).withIndex()) {
                s1 += expandPrettyPrint(id, input, components, level + 1)
            }
        }

        return s1
    }

    private fun compareComponentsByType(ic1: Component, ic2: Component): Int {
        val s1 = componentToSortableString(ic1)
        val s2 = componentToSortableString(ic2)

        return s1.compareTo(s2)
    }

    private fun componentToSortableString(ic1: Component): String {
        val s1 = when (ic1) {
            is AndGate -> "And"
            is XorGate -> "Xor"
            is OrGate -> "Or"
            is SignalWire -> "S"
            else -> ""
        }
        return s1
    }

    private fun part1(components: Map<String, Component>): String {
        var result = ""
        val circuit = mutableMapOf<String, Boolean?>()
        while (true) {
            var updated = false
            for (c in components) {
                if(!circuit.containsKey(c.key)) {
                    val v = c.value.getOutputValue(circuit)
                    if(v != null) {
                        updated = true
                        circuit[c.key] = v
                    }

                }
            }
            if(!updated) {
                break
            }
        }
        for (k in circuit.keys.sortedDescending()) {
            if (k.startsWith("z")) {
                val value = circuit.getValue(k)!!
                //println("$k: ${value}")
                result += if (value) '1' else '0'
            }
        }

        //println("Result: $result")
        return result
    }
}