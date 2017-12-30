package com.example.hoobie.adventofcode

import com.example.hoobie.adventofcode.utils.FileUtil
import com.example.hoobie.adventofcode.utils.patch
import kotlin.math.abs
import kotlin.math.min

/*
--- Day 11: Hex Ed ---

Crossing the bridge, you've barely reached the other side of the stream when a program comes up to you, clearly in distress. 
"It's my child process," she says, "he's gotten lost in an infinite grid!"

Fortunately for her, you have plenty of experience with infinite grids.

Unfortunately for you, it's a hex grid.

The hexagons ("hexes") in this grid are aligned such that adjacent hexes can be found to 
the north, northeast, southeast, south, southwest, and northwest:

  \ n  /
nw +--+ ne
  /    \
-+      +-
  \    /
sw +--+ se
  / s  \
You have the path the child process took. Starting where he started, 
you need to determine the fewest number of steps required to reach him. 
(A "step" means to move from the hex you are in to any adjacent hex.)

For example:

ne,ne,ne is 3 steps away.
ne,ne,sw,sw is 0 steps away (back where you started).
ne,ne,s,s is 2 steps away (se,se).
se,sw,se,sw,sw is 3 steps away (s,s,sw).

--- Part Two ---

How many steps away is the furthest he ever got from his starting position?
 */

object Day11HexEd {

    /*
     * [nw/se, n/s, ne/sw]
     */
    fun computeSteps(input: String): Pair<Int, Int> {
        val results = input.split(",")
                .fold(Pair(listOf(0, 0, 0), 0), { acc, step ->
                    val path = move(acc.first, step)

                    val vector = normalizePath(path)

                    val minSteps = abs(vector[0]) + abs(vector[1]) + abs(vector[2])
                    if (minSteps > acc.second) Pair(vector, minSteps) else Pair(vector, acc.second)
                })

        return Pair(abs(results.first[0]) + abs(results.first[1]) + abs(results.first[2]), results.second)
    }

    private fun move(vector: List<Int>, step: String): List<Int> {
        return when (step) {
            "nw" -> vector.patch(0, vector[0] + 1)
            "se" -> vector.patch(0, vector[0] - 1)

            "n" -> vector.patch(1, vector[1] + 1)
            "s" -> vector.patch(1, vector[1] - 1)

            "ne" -> vector.patch(2, vector[2] + 1)
            "sw" -> vector.patch(2, vector[2] - 1)

            else -> throw IllegalArgumentException()
        }
    }

    /*
     * Equations:
     * 1. sw + n = nw
     * 2. ne + s = se
     * 3. nw + ne = n
     * 4. se + sw = s
     * 5. se + n = ne
     * 6. nw + s = sw
     */
    private fun normalizePath(vector: List<Int>): List<Int> {
        val nw = min(if (vector[2] < 0) abs(vector[2]) else 0, if (vector[1] > 0) vector[1] else 0)
        val eq1 = vector
                .patch(0, vector[0] + nw)
                .patch(1, vector[1] - nw)
                .patch(2, vector[2] + nw)

        val se = min(if (eq1[2] > 0) eq1[2] else 0, if (eq1[1] < 0) abs(eq1[1]) else 0)
        val eq2 = eq1
                .patch(0, eq1[0] - se)
                .patch(1, eq1[1] + se)
                .patch(2, eq1[2] - se)

        val n = min(if (eq2[0] > 0) eq2[0] else 0, if (eq2[2] > 0) eq2[2] else 0)
        val eq3 = eq2
                .patch(0, eq2[0] - n)
                .patch(1, eq2[1] + n)
                .patch(2, eq2[2] - n)

        val s = min(if (eq3[0] < 0) abs(eq3[0]) else 0, if (eq3[2] < 0) abs(eq3[2]) else 0)
        val eq4 = eq3
                .patch(0, eq3[0] + s)
                .patch(1, eq3[1] - s)
                .patch(2, eq3[2] + s)

        val ne = min(if (eq4[0] < 0) abs(eq4[0]) else 0, if (eq4[1] > 0) eq4[1] else 0)
        val eq5 = eq4
                .patch(0, eq4[0] + ne)
                .patch(1, eq4[1] - ne)
                .patch(2, eq4[2] + ne)

        val sw = min(if (eq5[0] > 0) eq5[0] else 0, if (eq5[1] < 0) abs(eq5[1]) else 0)
        return eq5
                .patch(0, eq5[0] - sw)
                .patch(1, eq5[1] + sw)
                .patch(2, eq5[2] - sw)
    }

}

private val inputFileName = "day11.txt"

fun main(args: Array<String>) {
    val input = FileUtil.readFile(inputFileName)

    println("Steps: " + Day11HexEd.computeSteps(input))
}
