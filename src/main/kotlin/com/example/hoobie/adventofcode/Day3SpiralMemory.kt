package com.example.hoobie.adventofcode

import kotlin.math.abs
import kotlin.math.ceil
import kotlin.math.sqrt

/*
--- Day 3: Spiral Memory ---

You come across an experimental new kind of memory stored on an infinite two-dimensional grid.

Each square on the grid is allocated in a spiral pattern starting at a location marked 1 and then counting up 
while spiraling outward. For example, the first few squares are allocated like this:

17  16  15  14  13
18   5   4   3  12
19   6   1   2  11
20   7   8   9  10
21  22  23---> ...
While this is very space-efficient (no squares are skipped), requested data must be carried back to square 1 
(the location of the only access port for this memory system) by programs that can only move up, down, left, or right. 
They always take the shortest path: the Manhattan Distance between the location of the data and square 1.

For example:

Data from square 1 is carried 0 steps, since it's at the access port.
Data from square 12 is carried 3 steps, such as: down, left, left.
Data from square 23 is carried only 2 steps: up twice.
Data from square 1024 must be carried 31 steps.
How many steps are required to carry the data from the square identified in your puzzle input all the way to the access port?

--- Part Two ---

As a stress test on the system, the programs here clear the grid and then store the value 1 in square 1. 
Then, in the same allocation order as shown above, they store the sum of the values in all adjacent squares, including diagonals.

So, the first few squares' values are chosen as follows:

Square 1 starts with the value 1.
Square 2 has only one adjacent filled square (with value 1), so it also stores 1.
Square 3 has both of the above squares as neighbors and stores the sum of their values, 2.
Square 4 has all three of the aforementioned squares as neighbors and stores the sum of their values, 4.
Square 5 only has the first and fourth squares as neighbors, so it gets the value 5.
Once a square is written, its value does not change. Therefore, the first few squares would receive the following values:

147  142  133  122   59
304    5    4    2   57
330   10    1    1   54
351   11   23   25   26
362  747  806--->   ...
What is the first value written that is larger than your puzzle input?
 */

object Day3SpiralMemory {

    fun calculateDistance(input: Int): Int {
        var current = 1
        var direction = Pair(1, 0)
        var xRange = 0..0
        var yRange = 0..0
        var x = 0
        var y = 0

        while (current != input) {
            when (direction) {
                Pair(1, 0) -> xRange = xRange.first..xRange.last + 1
                Pair(-1, 0) -> xRange = xRange.first - 1..xRange.last
            }

            while (x in xRange && direction.first != 0) {
                if (current == input) break

                x += direction.first
                if (x == xRange.first && direction.first < 0) direction = Pair(0, 1)
                if (x == xRange.last && direction.first > 0) direction = Pair(0, -1)
                current++
            }

            when (direction) {
                Pair(0, 1) -> yRange = yRange.first..yRange.last + 1
                Pair(0, -1) -> yRange = yRange.first - 1..yRange.last
            }

            while (y in yRange && direction.second != 0) {
                if (current == input) break

                y += direction.second
                if (y == yRange.first && direction.second < 0) direction = Pair(-1, 0)
                if (y == yRange.last && direction.second > 0) direction = Pair(1, 0)
                current++
            }
        }

        return abs(x) + abs(y)
    }

    // TODO: refactor
    fun calculateFirstValueGreaterThan(input: Int): Int {
        val squareSize = ceil(sqrt(input.toDouble())).toInt()

        var current = 1
        var direction = Pair(1, 0)
        var xRange = squareSize..squareSize
        var yRange = squareSize..squareSize
        var x = squareSize
        var y = squareSize

        val memory = Array(squareSize * 2, { IntArray(squareSize * 2) })
        memory[x][y] = 1

        while (current <= input) {
            when (direction) {
                Pair(1, 0) -> xRange = xRange.first..xRange.last + 1
                Pair(-1, 0) -> xRange = xRange.first - 1..xRange.last
            }

            while (x in xRange && direction.first != 0) {
                if (current > input) break

                x += direction.first
                if (x == xRange.first && direction.first < 0) direction = Pair(0, 1)
                if (x == xRange.last && direction.first > 0) direction = Pair(0, -1)
                current = sumNeighbours(x, y, memory)
                memory[x][y] = current
            }

            when (direction) {
                Pair(0, 1) -> yRange = yRange.first..yRange.last + 1
                Pair(0, -1) -> yRange = yRange.first - 1..yRange.last
            }

            while (y in yRange && direction.second != 0) {
                if (current > input) break

                y += direction.second
                if (y == yRange.first && direction.second < 0) direction = Pair(-1, 0)
                if (y == yRange.last && direction.second > 0) direction = Pair(1, 0)
                current = sumNeighbours(x, y, memory)
                memory[x][y] = current
            }
        }

        return current
    }

    private fun sumNeighbours(x: Int, y: Int, memory: Array<IntArray>): Int {
        return memory[x][y] + memory[x - 1][y - 1] + memory[x - 1][y] + memory[x][y - 1] + memory[x + 1][y + 1] +
                memory[x + 1][y] + memory[x][y + 1] + memory[x + 1][y - 1] + memory[x - 1][y + 1]
    }

}

private val input = 277678

fun main(args: Array<String>) {
    println("Distance: " + Day3SpiralMemory.calculateDistance(input))
    println("Value greater then input: " + Day3SpiralMemory.calculateFirstValueGreaterThan(input))
}
