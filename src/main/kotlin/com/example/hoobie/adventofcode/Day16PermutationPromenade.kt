package com.example.hoobie.adventofcode

import com.example.hoobie.adventofcode.utils.FileUtil
import kotlin.math.max
import kotlin.math.min

/*
--- Day 16: Permutation Promenade ---
You come upon a very unusual sight; a group of programs here appear to be dancing.

There are sixteen programs in total, named a through p. They start by standing in a line: 
a stands in position 0, b stands in position 1, and so on until p, which stands in position 15.

The programs' dance consists of a sequence of dance moves:

Spin, written sX, makes X programs move from the end to the front, but maintain their order otherwise. 
(For example, s3 on abcde produces cdeab).
Exchange, written xA/B, makes the programs at positions A and B swap places.
Partner, written pA/B, makes the programs named A and B swap places.
For example, with only five programs standing in a line (abcde), they could do the following dance:

s1, a spin of size 1: eabcd.
x3/4, swapping the last two programs: eabdc.
pe/b, swapping programs e and b: baedc.
After finishing their dance, the programs end up in order baedc.

You watch the dance for a while and record their dance moves (your puzzle input). 
In what order are the programs standing after their dance?

--- Part Two ---
Now that you're starting to get a feel for the dance moves, you turn your attention to the dance as a whole.

Keeping the positions they ended up in from their previous dance, the programs perform it again and again: 
including the first dance, a total of one billion (1000000000) times.

In the example above, their second dance would begin with the order baedc, and use the same dance moves:

s1, a spin of size 1: cbaed.
x3/4, swapping the last two programs: cbade.
pe/b, swapping programs e and b: ceadb.
In what order are the programs standing after their billion dances?
 */

object Day16PermutationPromenade {

    fun getOrder(programs: List<String>, input: String): String {
        val programsConverted = programs.map { it[0] } // workaround for tests
        val moves = transformMoves(input)
        return getOrderFast(moves, programsConverted).joinToString("")
    }

    fun getOrderAfterOneBillionTimes(programs: List<String>, input: String): String {
        val programsConverted = programs.map { it[0] } // workaround for tests
        val moves = transformMoves(input)
        val cycle = findCycle(1, moves, programsConverted, programsConverted)
        val loop = 1000000000 % cycle
        return (1..loop).fold(programsConverted, { acc: List<Char>, _ ->
            getOrderFast(moves, acc)
        }).joinToString("")
    }

    private fun transformMoves(input: String): List<DanceMove> {
        return input.split(",").map {
            when (it[0]) {
                's' -> Spin(it.drop(1).toInt())
                'x' -> {
                    val indexes = it.drop(1).split("/")
                    Exchange(indexes[0].toInt(), indexes[1].toInt())
                }
                'p' -> Partner(it[1], it[3])
                else -> throw IllegalArgumentException()
            }
        }
    }

    private tailrec fun findCycle(acc: Int, moves: List<DanceMove>, programs: List<Char>, firstOrder: List<Char>): Int {
        val order = getOrderFast(moves, programs)
        if (order == firstOrder) return acc
        return findCycle(acc + 1, moves, order, firstOrder)
    }

    private fun getOrderFast(moves: List<DanceMove>, programs: List<Char>): List<Char> {
        return moves.fold(programs, { acc, danceMove ->
            danceMove.make(acc)
        })
    }

    private abstract class DanceMove {
        abstract fun make(programs: List<Char>): List<Char>
    }

    private data class Spin(private val x: Int) : DanceMove() {
        override fun make(programs: List<Char>): List<Char> {
            return programs.spin(x)
        }
    }

    private data class Exchange(private val a: Int, private val b: Int) : DanceMove() {
        override fun make(programs: List<Char>): List<Char> {
            return programs.swap(a, b)
        }
    }

    private data class Partner(private val a: Char, private val b: Char) : DanceMove() {
        override fun make(programs: List<Char>): List<Char> {
            return programs.swap(a, b)
        }
    }

    // List extensions

    private fun <T> List<T>.spin(x: Int) = this.takeLast(x) + this.dropLast(x)

    private fun <T> List<T>.swap(elem1: T, elem2: T): List<T> {
        val idx1 = this.indexOf(elem1)
        val idx2 = this.indexOf(elem2)
        return this.swap(idx1, idx2)
    }

    private fun <T> List<T>.swap(idx1: Int, idx2: Int): List<T> {
        if (idx1 == idx2) return this

        val lowerIdx = min(idx1, idx2)
        val higherIdx = max(idx1, idx2)

        return this.take(lowerIdx) +
                this[higherIdx] +
                (if (higherIdx - lowerIdx > 1) this.subList(lowerIdx + 1, higherIdx) else listOf()) +
                this[lowerIdx] +
                this.drop(higherIdx + 1)
    }
}

private val inputFileName = "day16.txt"

fun main(args: Array<String>) {
    val input = FileUtil.readFile(inputFileName)

    println("Order part 1: " + Day16PermutationPromenade.getOrder(('a'..'p').toList().map { it.toString() }, input))
    println("Order part 2: " + Day16PermutationPromenade.getOrderAfterOneBillionTimes(('a'..'p').toList().map { it.toString() }, input))
}
