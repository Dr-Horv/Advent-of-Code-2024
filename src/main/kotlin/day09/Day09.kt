package se.horv.day09

import se.horv.util.Solver
import kotlin.math.max
import kotlin.math.min

data class Section(val id: Int, var blocks: Int, var free: Int)

enum class Type {
    BLOCKS,
    FREE
}

data class Node (val id: Int, var size: Int, val type: Type, var left: Node? = null, var right: Node? = null) {
    fun addAfter(n: Node) {
        val meRight = right
        right = n
        n.left = this
        n.right = meRight
        meRight?.left = n
    }

    fun replace(n: Node) {
        val meRight = right
        val meLeft = left

        meLeft?.right = n
        n.left = meLeft

        meRight?.left = n
        n.right = meRight
    }
}

class Day09: Solver {
    override fun solve(lines: List<String>, partTwo: Boolean): String {
        val sections = mutableListOf<Section>()
        val input = lines.first().toMutableList()
        input.add('0')
        var i = 0
        while (input.isNotEmpty()) {
            sections.add(Section(i, input.removeFirst().digitToInt(), input.removeFirst().digitToInt()))
            i++
        }

        val compressedSections = if(!partTwo) {
            compress1(sections)
        } else {
            compress2(sections)
        }


        var pos = 0
        var checksum = 0L

        for (section in compressedSections) {
            for (l in 1.. max(section.blocks, section.free)) {
                if(section.id != -1) {
                    checksum += section.id * pos
                }
                pos++
            }
        }
        return checksum.toString()
    }


    private fun compress2(sections: MutableList<Section>): List<Section> {
        val (start, end) = createLinkedMemoryList(sections)
        compressLinkedMemory(start, end)
        return sectionsFromLinkedMemoryList(start)
    }

    private fun compressLinkedMemory(start: Node, end: Node) {
        var toMove: Node? = end
        while (toMove != null) {
            if (toMove.type == Type.FREE) {
                toMove = toMove.left
                continue
            }

            var search: Node? = start
            while (search != null && toMove != null && search != toMove) {
                if (search.type == Type.BLOCKS) {
                    search = search.right
                    continue
                }

                if (search.size < toMove.size) {
                    search = search.right
                    continue
                }

                val empty = Node(-1, toMove.size, Type.FREE)
                toMove.replace(empty)
                search.replace(toMove)
                if (search.size > toMove.size) {
                    val free = Node(-1, search.size - toMove.size, Type.FREE)
                    toMove.addAfter(free)
                }
                toMove = empty
                break
            }
            toMove = toMove?.left
        }
    }

    private fun sectionsFromLinkedMemoryList(start: Node): MutableList<Section> {
        val result = mutableListOf<Section>()
        var p: Node? = start
        while (p != null) {
            result.add(
                if (p.type == Type.BLOCKS) {
                    Section(p.id, blocks = p.size, 0)
                } else {
                    Section(-1, 0, free = p.size)
                }
            )
            p = p.right
        }
        return result
    }

    private fun createLinkedMemoryList(sections: MutableList<Section>): Pair<Node, Node> {
        val first = sections.first()
        var curr = Node(first.id, first.blocks, Type.BLOCKS)
        val start = curr
        val next = Node(-1, first.free, Type.FREE)
        start.addAfter(next)
        curr = next

        for (section in sections.drop(1)) {
            val n = Node(section.id, section.blocks, Type.BLOCKS)
            val f = Node(-1, section.free, Type.FREE)
            curr.addAfter(n)
            n.addAfter(f)
            curr = f
        }
        return Pair(start, curr)
    }

    private fun printMemory(start: Node) {
        var curr: Node? = start
        var s = ""
        while (curr != null) {
            s += if(curr.type == Type.FREE) {
                ".".repeat(curr.size)
            } else {
                curr.id.toString().repeat(curr.size)
            }
            curr = curr.right
        }
        println(s)
    }

    private fun compress1(sections: MutableList<Section>): List<Section> {
        var startIndex = 0
        var endIndex = sections.lastIndex
        var moveTo = sections[startIndex]
        var moveFrom = sections[endIndex]
        val compressedSections = mutableListOf<Section>()
        while (startIndex <= endIndex) {
            if (moveTo.blocks >= 0) {
                compressedSections.add(Section(id = moveTo.id, blocks = moveTo.blocks, free = 0))
                moveTo.blocks = 0
            }
            if (moveTo.free == 0) {
                startIndex++
                moveTo = sections[startIndex]
                continue
            }
            if (moveFrom.blocks == 0) {
                endIndex--
                moveFrom = sections[endIndex]
                continue
            }

            val blocksToMove = min(moveTo.free, moveFrom.blocks)
            moveTo.free -= blocksToMove
            moveFrom.blocks -= blocksToMove
            compressedSections.add(Section(id = moveFrom.id, blocks = blocksToMove, free = 0))
        }
        if (moveFrom.blocks > 0) {
            compressedSections.add(Section(id = moveFrom.id, blocks = moveFrom.blocks, free = 0))
        }
        return compressedSections
    }
}