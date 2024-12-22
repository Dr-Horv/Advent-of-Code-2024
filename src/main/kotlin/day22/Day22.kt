package se.horv.day22

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import se.horv.util.Solver
import kotlin.time.DurationUnit
import kotlin.time.measureTime

data class SecretGenerator(val initial: Long) {
    private var secret = initial
    fun next(): Long {
        secret = mix(secret * 64L)
        secret = prune()
        secret = mix(secret / 32L )
        secret = prune()
        secret = mix(secret * 2048)
        secret = prune()
        return secret
    }

    private fun mix(result: Long): Long = secret xor result
    private fun prune(): Long = secret % 16777216L
}

class Day22: Solver {
    override fun solve(lines: List<String>, partTwo: Boolean): String {
        val initialSecrets = lines.map { it.toLong() }
        return if(!partTwo) {
            part1(initialSecrets)
        } else {
            part2(initialSecrets)
        }
    }

    private fun part1(initialSecrets: List<Long>): String = initialSecrets.sumOf {
        val g = SecretGenerator(it)
        repeat(1999) { g.next() }
        g.next()
    }.toString()

    private fun part2(initialSecrets: List<Long>, loopSize: Int = 2000): String {
        val cachedG = mutableMapOf<Int, Map<List<Long>, Long>>()
        val allPossibleSequences = mutableSetOf<List<Long>>()
        println("Calculating values...")
        val duration = measureTime {
            runBlocking {
                initialSecrets.withIndex().map { (gi, initialSecret) ->
                    async(Dispatchers.Default) {
                        val setOfSequences = mutableSetOf<List<Long>>()
                        val valueFromSequence = mutableMapOf<List<Long>, Long>()
                        val g = SecretGenerator(initialSecret)
                        var last = g.initial.toString().last().digitToInt().toLong()
                        val sequence = mutableListOf<Long>()
                        for (i in 1..loopSize) {
                            val n = g.next().toString().last().digitToInt().toLong()
                            val diff = n - last
                            if(sequence.size == 4) {
                                sequence.removeFirst()
                            }
                            sequence.add(diff)
                            if(sequence.size == 4 && !setOfSequences.contains(sequence)) {
                                val s = sequence.toList()
                                setOfSequences.add(s)
                                valueFromSequence[s] = n
                            }
                            last = n
                        }
                        cachedG[gi] = valueFromSequence
                        setOfSequences
                    }
                }.awaitAll().forEach { allPossibleSequences.addAll(it) }
            }
        }


        println("Calculated all values in ${duration.toString(DurationUnit.MILLISECONDS)}, searching for best sequence...")
        var best: Pair<List<Long>, Long>? = null
        val chunkSize = allPossibleSequences.size / 64
        val d2 = measureTime {
            val chunkValues = runBlocking {
                allPossibleSequences.chunked(chunkSize)
                    .map { chunk ->
                        async(Dispatchers.Default) {
                            val values = chunk.map { s ->
                                s to cachedG.values.sumOf { c -> c.getOrDefault(s, 0) }
                            }
                            values.maxBy { it.second }
                        }
                    }.awaitAll()
            }
            best = chunkValues.maxBy { it.second }
        }

        val b = best!!
        println("Best sequence found: ${b.first} in ${d2.toString(DurationUnit.MILLISECONDS)}")

        return b.second.toString()
    }

    private fun testSequence(sequenceToTest: List<Long>, initialSecrets: List<Long>): Long {
        var bananas = 0L
        for (initialSecret in initialSecrets) {
            val g = SecretGenerator(initialSecret)
            var last = g.initial.toString().last().digitToInt().toLong()
            val sequence = mutableListOf<Long>()
            for (i in 1..2000) {
                val n = g.next().toString().last().digitToInt().toLong()
                val diff = n - last
                if(sequence.size == 4) {
                    sequence.removeFirst()
                }
                sequence.add(diff)
                if(sequence.size == 4 && sequenceToTest == sequence) {
                    bananas += n
                    break
                }
                last = n
            }
        }
        return bananas
    }
}