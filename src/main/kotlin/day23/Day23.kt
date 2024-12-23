package se.horv.day23

import se.horv.util.Solver

data class Node(val name: String, val neighbours: MutableSet<Node> = mutableSetOf()) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Node

        return name == other.name
    }

    override fun hashCode(): Int {
        return name.hashCode()
    }

    override fun toString(): String {
        return "Node(name='$name', neighbours=${neighbours.map { it.name }})"
    }
}

class Day23: Solver {
    override fun solve(lines: List<String>, partTwo: Boolean): String {
        val nodes = parseNodes(lines)
        return if(!partTwo) {
            part1(nodes)
        } else {
            part2(nodes)

        }
    }

    private fun part1(nodes: Map<String, Node>): String {
        val sets = mutableSetOf<Set<Node>>()
        for (node in nodes.values.sortedByDescending { it.neighbours.size }) {
            val setsOfNode = findSetsOfSize(node, 3)
            sets.addAll(setsOfNode)
        }
        val containsT = sets.filter { set ->
            set.any { n -> n.name.startsWith('t') }
        }

        return containsT.size.toString()
    }

    private fun part2(nodes: Map<String, Node>): String {
        val mostNeighboursFirst = nodes.values.sortedByDescending { it.neighbours.size }
        val alternatives = mutableListOf<String>()
        for (n1 in mostNeighboursFirst) {
            for (n2 in mostNeighboursFirst) {
                if(n1 == n2) continue
                if(n1.neighbours.contains(n2)) {
                    val common = n1.neighbours.filter { n2.neighbours.contains(it) }.toMutableSet()
                    common.add(n1)
                    common.add(n2)
                    val test = common.all { commonNode ->
                        val others = common.filter { it != commonNode }
                        commonNode.neighbours.containsAll(others)
                    }
                    if(test) {
                        val s = common.map { it.name }.sorted().joinToString(",")
                        alternatives.add(s)
                    }
                }
            }
        }

        return alternatives.maxBy { it.length }
    }

    private fun findSetsOfSize(node: Node, maxSize: Int): Collection<Set<Node>> {
        val sets = search(mutableSetOf(node), node.neighbours, maxSize)
        return sets
    }

    private fun search(
        current: Set<Node>,
        candidates: Set<Node>,
        setSize: Int
    ): Collection<Set<Node>> {
        if(current.size == setSize) {
            return setOf(current)
        }

        val result = mutableSetOf<Set<Node>>()
        for(c in candidates) {
            if(current.all { c.neighbours.contains(it) }) {
                result.addAll(search(current + c, candidates - c, setSize))
            }
        }
        return result
    }

    private fun parseNodes(lines: List<String>): Map<String, Node> {
        val nodes = mutableMapOf<String, Node>()
        for (line in lines) {
            for (name in line.split("-")) {
                if (!nodes.containsKey(name)) {
                    nodes[name] = Node(name)
                }
            }
        }

        for (line in lines) {
            val names = line.split("-")
            val n1 = names[0]
            val n2 = names[1]
            nodes[n1]!!.neighbours.add(nodes[n2]!!)
            nodes[n2]!!.neighbours.add(nodes[n1]!!)
        }

        return nodes
    }
}