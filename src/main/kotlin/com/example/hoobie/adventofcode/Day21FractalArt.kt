package com.example.hoobie.adventofcode

import com.example.hoobie.adventofcode.utils.FileUtil

/*
--- Day 21: Fractal Art ---
You find a program trying to generate some art. It uses a strange process that involves repeatedly enhancing the detail of an image through a set of rules.

The image consists of a two-dimensional square grid of pixels that are either on (#) or off (.). The program always begins with this pattern:

.#.
..#
###
Because the pattern is both 3 pixels wide and 3 pixels tall, it is said to have a size of 3.

Then, the program repeats the following process:

If the size is evenly divisible by 2, break the pixels up into 2x2 squares, and convert each 2x2 square into a 3x3 square by following the corresponding enhancement rule.
Otherwise, the size is evenly divisible by 3; break the pixels up into 3x3 squares, and convert each 3x3 square into a 4x4 square by following the corresponding enhancement rule.
Because each square of pixels is replaced by a larger one, the image gains pixels and so its size increases.

The artist's book of enhancement rules is nearby (your puzzle input); however, it seems to be missing rules. The artist explains that sometimes, one must rotate or flip the input pattern to find a match. (Never rotate or flip the output pattern, though.) Each pattern is written concisely: rows are listed as single units, ordered top-down, and separated by slashes. For example, the following rules correspond to the adjacent patterns:

../.#  =  ..
          .#

                .#.
.#./..#/###  =  ..#
                ###

                        #..#
#..#/..../#..#/.##.  =  ....
                        #..#
                        .##.
When searching for a rule to use, rotate and flip the pattern as necessary. For example, all of the following patterns match the same rule:

.#.   .#.   #..   ###
..#   #..   #.#   ..#
###   ###   ##.   .#.
Suppose the book contained the following two rules:

../.# => ##./#../...
.#./..#/### => #..#/..../..../#..#
As before, the program begins with this pattern:

.#.
..#
###
The size of the grid (3) is not divisible by 2, but it is divisible by 3. It divides evenly into a single square; the square matches the second rule, which produces:

#..#
....
....
#..#
The size of this enhanced grid (4) is evenly divisible by 2, so that rule is used. It divides evenly into four squares:

#.|.#
..|..
--+--
..|..
#.|.#
Each of these squares matches the same rule (../.# => ##./#../...), three of which require some flipping and rotation to line up with the rule. The output for the rule is the same in all four cases:

##.|##.
#..|#..
...|...
---+---
##.|##.
#..|#..
...|...
Finally, the squares are joined into a new grid:

##.##.
#..#..
......
##.##.
#..#..
......
Thus, after 2 iterations, the grid contains 12 pixels that are on.

How many pixels stay on after 5 iterations?

--- Part Two ---
How many pixels stay on after 18 iterations?

 */
object Day21FractalArt {

    private val startPattern = ".#./..#/###"

    fun countTurnedOnPixels(input: String, iterations: Int): Int {
        val rules = readRules(input)

        val pattern = (0 until iterations).fold(startPattern, { acc, _ ->
            val divided = divide(acc)

            val transformed = divided.map {
                it.map { transform(it, rules) }
            }

            merge(transformed)
        })

        return pattern.toCharArray().fold(0, { acc, c ->
            if (c == '#') acc + 1
            else acc
        })
    }

    private fun readRules(input: String): Map<String, String> {
        return input.split("\n")
                .map {
                    val tokens = it.split(" => ")
                    Pair(tokens[0], tokens[1])
                }.toMap()
    }

    private fun divide(pattern: String): List<List<String>> {
        val lines = pattern.split("/")
        val size = lines[0].length

        if (size == 2 || size == 3) return listOf(listOf(pattern))

        return if (size % 2 == 0 && size > 2)
            divide(lines, 2)
        else if (size % 3 == 0 && size > 3)
            divide(lines, 3)
        else throw IllegalArgumentException("divide")
    }

    private fun divide(lines: List<String>, divider: Int): List<List<String>> =
            lines
                    .map { it.chunked(divider) }.chunked(divider)
                    .map { groupByIndex(it) }
                    .map { it.map { it.joinToString("/") } }

    private fun merge(dividedPattern: List<List<String>>): String =
            dividedPattern.map { it.map { it.split("/") } }
                    .map { groupByIndex(it) }
                    .joinToString("/") {
                        it.joinToString("/") {
                            it.joinToString("")
                        }
                    }

    private fun groupByIndex(it: List<List<String>>): List<List<String>> =
            it.fold(linkedMapOf(), { acc: Map<Int, List<String>>, list ->
                val pairs = list.mapIndexed { i, elem -> Pair(i, listOf(elem)) }
                val newPairs = pairs.map { Pair(it.first, acc[it.first]?.plus(it.second) ?: it.second) }
                acc + newPairs
            }).values.toList()

    private tailrec fun transform(pattern: String, rules: Map<String, String>, rotateCounter: Int = 0): String {
        if (rotateCounter > 3) throw IllegalArgumentException("transform")

        if (pattern in rules) return rules[pattern]!!

        val horizontalFlip = flipHorizontally(pattern)
        if (horizontalFlip in rules) return rules[horizontalFlip]!!
        if (flipVertically(pattern) in rules) return rules[flipVertically(pattern)]!!
        if (flipVertically(horizontalFlip) in rules) return rules[flipVertically(horizontalFlip)]!!

        return transform(rotate(pattern), rules, rotateCounter + 1)
    }

    private fun rotate(pattern: String) = flipVertically(transpose(pattern))
    private fun flipHorizontally(pattern: String) = pattern.split("/").joinToString("/") { it.reversed() }
    private fun flipVertically(pattern: String) = pattern.split("/").asReversed().joinToString("/")
    private fun transpose(pattern: String): String {
        val size = pattern.split("/")[0].length

        return pattern.replace("/", "").toCharArray()
                .foldIndexed(hashMapOf(), { i, acc: Map<Int, List<Char>>, c ->
                    acc + Pair(i % size, acc[i % size]?.plus(c) ?: listOf(c))
                })
                .values.joinToString("/") { it.joinToString("") }
    }

}

private val inputFileName = "day21.txt"

fun main(args: Array<String>) {
    val input = FileUtil.readFile(inputFileName)

    println("Turned on pixels after 5 iterations: " + Day21FractalArt.countTurnedOnPixels(input, 5))
    println("Turned on pixels after 18 iterations: " + Day21FractalArt.countTurnedOnPixels(input, 18))
}
