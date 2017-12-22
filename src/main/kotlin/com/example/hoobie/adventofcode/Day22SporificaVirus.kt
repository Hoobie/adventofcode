package com.example.hoobie.adventofcode

import com.example.hoobie.adventofcode.utils.FileUtil
import com.example.hoobie.adventofcode.utils.patch

/*
--- Day 22: Sporifica Virus ---
Diagnostics indicate that the local grid computing cluster has been contaminated with the Sporifica Virus. 
The grid computing cluster is a seemingly-infinite two-dimensional grid of compute nodes. 
Each node is either clean or infected by the virus.

To prevent overloading the nodes (which would render them useless to the virus) or detection by system administrators, 
exactly one virus carrier moves through the network, infecting or cleaning nodes as it moves. 
The virus carrier is always located on a single node in the network (the current node) and keeps track of the direction it is facing.

To avoid detection, the virus carrier works in bursts; in each burst, it wakes up, does some work, 
and goes back to sleep. The following steps are all executed in order one time each burst:

If the current node is infected, it turns to its right. Otherwise, it turns to its left. 
(Turning is done in-place; the current node does not change.)
If the current node is clean, it becomes infected. Otherwise, it becomes cleaned. 
(This is done after the node is considered for the purposes of changing direction.)
The virus carrier moves forward one node in the direction it is facing.
Diagnostics have also provided a map of the node infection status (your puzzle input). 
Clean nodes are shown as .; infected nodes are shown as #. This map only shows the center of the grid; 
there are many more nodes beyond those shown, but none of them are currently infected.

The virus carrier begins in the middle of the map facing up.

For example, suppose you are given a map like this:

..#
#..
...
Then, the middle of the infinite grid looks like this, with the virus carrier's position marked with [ ]:

. . . . . . . . .
. . . . . . . . .
. . . . . . . . .
. . . . . # . . .
. . . #[.]. . . .
. . . . . . . . .
. . . . . . . . .
. . . . . . . . .
The virus carrier is on a clean node, so it turns left, infects the node, and moves left:

. . . . . . . . .
. . . . . . . . .
. . . . . . . . .
. . . . . # . . .
. . .[#]# . . . .
. . . . . . . . .
. . . . . . . . .
. . . . . . . . .
The virus carrier is on an infected node, so it turns right, cleans the node, and moves up:

. . . . . . . . .
. . . . . . . . .
. . . . . . . . .
. . .[.]. # . . .
. . . . # . . . .
. . . . . . . . .
. . . . . . . . .
. . . . . . . . .
Four times in a row, the virus carrier finds a clean, infects it, turns left, and moves forward, ending 
in the same place and still facing up:

. . . . . . . . .
. . . . . . . . .
. . . . . . . . .
. . #[#]. # . . .
. . # # # . . . .
. . . . . . . . .
. . . . . . . . .
. . . . . . . . .
Now on the same node as before, it sees an infection, which causes it to turn right, clean the node, and move forward:

. . . . . . . . .
. . . . . . . . .
. . . . . . . . .
. . # .[.]# . . .
. . # # # . . . .
. . . . . . . . .
. . . . . . . . .
. . . . . . . . .
After the above actions, a total of 7 bursts of activity had taken place. Of them, 5 bursts of activity caused an infection.

After a total of 70, the grid looks like this, with the virus carrier facing up:

. . . . . # # . .
. . . . # . . # .
. . . # . . . . #
. . # . #[.]. . #
. . # . # . . # .
. . . . . # # . .
. . . . . . . . .
. . . . . . . . .
By this time, 41 bursts of activity caused an infection (though most of those nodes have since been cleaned).

After a total of 10000 bursts of activity, 5587 bursts will have caused an infection.

Given your actual map, after 10000 bursts of activity, how many bursts cause a node to become infected? 
(Do not count nodes that begin infected.)
 */
object Day22SporificaVirus {

    fun countInfections(input: String, bursts: Int): Long {
        val grid = input.split("\n")
                .map { it.toCharArray().toList() }

        val middle = grid.size / 2
        
        return work(middle, middle, Pair(0, -1), grid, bursts, 0L)
    }

    private tailrec fun work(x: Int, y: Int, direction: Pair<Int, Int>, grid: List<List<Char>>, bursts: Int, infections: Long): Long {
        if (bursts <= 0) return infections

        val enlargedGrid = if (x < 0 || x >= grid.size || y < 0 || y >= grid.size) enlargeGrid(grid) else grid
        val newX = if (enlargedGrid.size != grid.size) x + grid.size else x
        val newY = if (enlargedGrid.size != grid.size) y + grid.size else y
        
        val node = enlargedGrid[newY][newX]

        val newDirection = turn(node, direction)
        val newGrid = enlargedGrid.patch(newX, newY, if (node == '.') '#' else '.')
        val newInfections = if (node == '.') infections + 1 else infections
        
        return work(newX + newDirection.first, newY + newDirection.second, newDirection, newGrid, bursts - 1, newInfections)
    }

    private fun enlargeGrid(grid: List<List<Char>>): List<List<Char>> {
        val dots = getListOfDots((grid.size * 3) - 1)
        val prefixAndSuffix = (0 until grid.size - 1).fold(listOf(dots), { acc: List<List<Char>>, _ ->
            acc + listOf(dots)
        })

        val innerDots = getListOfDots(grid.size - 1)
        val g = grid.map { innerDots + it + innerDots }

        return prefixAndSuffix + g + prefixAndSuffix
    }

    private fun getListOfDots(size: Int) = ("." * size).toCharArray().toList()

    private fun turn(node: Char, direction: Pair<Int, Int>): Pair<Int, Int> {
        if (node == '#') {
            return when (direction) {
                Pair(0, -1) -> Pair(1, 0)
                Pair(1, 0) -> Pair(0, 1)
                Pair(0, 1) -> Pair(-1, 0)
                Pair(-1, 0) -> Pair(0, -1)
                else -> throw IllegalArgumentException("turn")
            }
        } else {
            return when (direction) {
                Pair(0, -1) -> Pair(-1, 0)
                Pair(-1, 0) -> Pair(0, 1)
                Pair(0, 1) -> Pair(1, 0)
                Pair(1, 0) -> Pair(0, -1)
                else -> throw IllegalArgumentException("turn")
            }
        }
    }

    private operator fun String.times(times: Int): String = (0 until times).fold(this, { acc, _ -> acc + this })

}

private val inputFileName = "day22.txt"

fun main(args: Array<String>) {
    val input = FileUtil.readFile(inputFileName)

    println("Infections: " + Day22SporificaVirus.countInfections(input, 10000))
}
