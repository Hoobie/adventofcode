package com.example.hoobie.adventofcode

import com.example.hoobie.adventofcode.utils.FileUtil

/*
--- Day 13: Packet Scanners ---

You need to cross a vast firewall. The firewall consists of several layers, each with a security scanner 
that moves back and forth across the layer. To succeed, you must not be detected by a scanner.

By studying the firewall briefly, you are able to record (in your puzzle input) the depth of each layer 
and the range of the scanning area for the scanner within it, written as depth: range. Each layer has a thickness of exactly 1. 
A layer at depth 0 begins immediately inside the firewall; a layer at depth 1 would start immediately after that.

For example, suppose you've recorded the following:

0: 3
1: 2
4: 4
6: 4
This means that there is a layer immediately inside the firewall (with range 3), a second layer immediately 
after that (with range 2), a third layer which begins at depth 4 (with range 4), and a fourth layer 
which begins at depth 6 (also with range 4). Visually, it might look like this:

 0   1   2   3   4   5   6
[ ] [ ] ... ... [ ] ... [ ]
[ ] [ ]         [ ]     [ ]
[ ]             [ ]     [ ]
                [ ]     [ ]
Within each layer, a security scanner moves back and forth within its range. 
Each security scanner starts at the top and moves down until it reaches the bottom, 
then moves up until it reaches the top, and repeats. A security scanner takes one picosecond to move one step. 
Drawing scanners as S, the first few picoseconds look like this:


Picosecond 0:
 0   1   2   3   4   5   6
[S] [S] ... ... [S] ... [S]
[ ] [ ]         [ ]     [ ]
[ ]             [ ]     [ ]
                [ ]     [ ]

Picosecond 1:
 0   1   2   3   4   5   6
[ ] [ ] ... ... [ ] ... [ ]
[S] [S]         [S]     [S]
[ ]             [ ]     [ ]
                [ ]     [ ]

Picosecond 2:
 0   1   2   3   4   5   6
[ ] [S] ... ... [ ] ... [ ]
[ ] [ ]         [ ]     [ ]
[S]             [S]     [S]
                [ ]     [ ]

Picosecond 3:
 0   1   2   3   4   5   6
[ ] [ ] ... ... [ ] ... [ ]
[S] [S]         [ ]     [ ]
[ ]             [ ]     [ ]
                [S]     [S]
Your plan is to hitch a ride on a packet about to move through the firewall. 
The packet will travel along the top of each layer, and it moves at one layer per picosecond. Each picosecond, 
the packet moves one layer forward (its first move takes it into layer 0), and then the scanners move one step. 
If there is a scanner at the top of the layer as your packet enters it, you are caught. 
(If a scanner moves into the top of its layer while you are there, you are not caught: 
it doesn't have time to notice you before you leave.) If you were to do this in the configuration above, 
marking your current position with parentheses, your passage through the firewall would look like this:

Initial state:
 0   1   2   3   4   5   6
[S] [S] ... ... [S] ... [S]
[ ] [ ]         [ ]     [ ]
[ ]             [ ]     [ ]
                [ ]     [ ]

Picosecond 0:
 0   1   2   3   4   5   6
(S) [S] ... ... [S] ... [S]
[ ] [ ]         [ ]     [ ]
[ ]             [ ]     [ ]
                [ ]     [ ]

 0   1   2   3   4   5   6
( ) [ ] ... ... [ ] ... [ ]
[S] [S]         [S]     [S]
[ ]             [ ]     [ ]
                [ ]     [ ]


Picosecond 1:
 0   1   2   3   4   5   6
[ ] ( ) ... ... [ ] ... [ ]
[S] [S]         [S]     [S]
[ ]             [ ]     [ ]
                [ ]     [ ]

 0   1   2   3   4   5   6
[ ] (S) ... ... [ ] ... [ ]
[ ] [ ]         [ ]     [ ]
[S]             [S]     [S]
                [ ]     [ ]


Picosecond 2:
 0   1   2   3   4   5   6
[ ] [S] (.) ... [ ] ... [ ]
[ ] [ ]         [ ]     [ ]
[S]             [S]     [S]
                [ ]     [ ]

 0   1   2   3   4   5   6
[ ] [ ] (.) ... [ ] ... [ ]
[S] [S]         [ ]     [ ]
[ ]             [ ]     [ ]
                [S]     [S]


Picosecond 3:
 0   1   2   3   4   5   6
[ ] [ ] ... (.) [ ] ... [ ]
[S] [S]         [ ]     [ ]
[ ]             [ ]     [ ]
                [S]     [S]

 0   1   2   3   4   5   6
[S] [S] ... (.) [ ] ... [ ]
[ ] [ ]         [ ]     [ ]
[ ]             [S]     [S]
                [ ]     [ ]


Picosecond 4:
 0   1   2   3   4   5   6
[S] [S] ... ... ( ) ... [ ]
[ ] [ ]         [ ]     [ ]
[ ]             [S]     [S]
                [ ]     [ ]

 0   1   2   3   4   5   6
[ ] [ ] ... ... ( ) ... [ ]
[S] [S]         [S]     [S]
[ ]             [ ]     [ ]
                [ ]     [ ]


Picosecond 5:
 0   1   2   3   4   5   6
[ ] [ ] ... ... [ ] (.) [ ]
[S] [S]         [S]     [S]
[ ]             [ ]     [ ]
                [ ]     [ ]

 0   1   2   3   4   5   6
[ ] [S] ... ... [S] (.) [S]
[ ] [ ]         [ ]     [ ]
[S]             [ ]     [ ]
                [ ]     [ ]


Picosecond 6:
 0   1   2   3   4   5   6
[ ] [S] ... ... [S] ... (S)
[ ] [ ]         [ ]     [ ]
[S]             [ ]     [ ]
                [ ]     [ ]

 0   1   2   3   4   5   6
[ ] [ ] ... ... [ ] ... ( )
[S] [S]         [S]     [S]
[ ]             [ ]     [ ]
                [ ]     [ ]
In this situation, you are caught in layers 0 and 6, because your packet entered the layer when its scanner 
was at the top when you entered it. You are not caught in layer 1, since the scanner moved into the top of the layer once you were already there.

The severity of getting caught on a layer is equal to its depth multiplied by its range. 
(Ignore layers in which you do not get caught.) The severity of the whole trip is the sum of these values. 
In the example above, the trip severity is 0*3 + 6*4 = 24.

Given the details of the firewall you've recorded, if you leave immediately, what is the severity of your whole trip?

--- Part Two ---

Now, you need to pass through the firewall without being caught - easier said than done.

You can't control the speed of the packet, but you can delay it any number of picoseconds. 
For each picosecond you delay the packet before beginning your trip, all security scanners move one step. 
You're not in the firewall during this time; you don't enter layer 0 until you stop delaying the packet.

In the example above, if you delay 10 picoseconds (picoseconds 0 - 9), you won't get caught:

State after delaying:
 0   1   2   3   4   5   6
[ ] [S] ... ... [ ] ... [ ]
[ ] [ ]         [ ]     [ ]
[S]             [S]     [S]
                [ ]     [ ]

Picosecond 10:
 0   1   2   3   4   5   6
( ) [S] ... ... [ ] ... [ ]
[ ] [ ]         [ ]     [ ]
[S]             [S]     [S]
                [ ]     [ ]

 0   1   2   3   4   5   6
( ) [ ] ... ... [ ] ... [ ]
[S] [S]         [S]     [S]
[ ]             [ ]     [ ]
                [ ]     [ ]


Picosecond 11:
 0   1   2   3   4   5   6
[ ] ( ) ... ... [ ] ... [ ]
[S] [S]         [S]     [S]
[ ]             [ ]     [ ]
                [ ]     [ ]

 0   1   2   3   4   5   6
[S] (S) ... ... [S] ... [S]
[ ] [ ]         [ ]     [ ]
[ ]             [ ]     [ ]
                [ ]     [ ]


Picosecond 12:
 0   1   2   3   4   5   6
[S] [S] (.) ... [S] ... [S]
[ ] [ ]         [ ]     [ ]
[ ]             [ ]     [ ]
                [ ]     [ ]

 0   1   2   3   4   5   6
[ ] [ ] (.) ... [ ] ... [ ]
[S] [S]         [S]     [S]
[ ]             [ ]     [ ]
                [ ]     [ ]


Picosecond 13:
 0   1   2   3   4   5   6
[ ] [ ] ... (.) [ ] ... [ ]
[S] [S]         [S]     [S]
[ ]             [ ]     [ ]
                [ ]     [ ]

 0   1   2   3   4   5   6
[ ] [S] ... (.) [ ] ... [ ]
[ ] [ ]         [ ]     [ ]
[S]             [S]     [S]
                [ ]     [ ]


Picosecond 14:
 0   1   2   3   4   5   6
[ ] [S] ... ... ( ) ... [ ]
[ ] [ ]         [ ]     [ ]
[S]             [S]     [S]
                [ ]     [ ]

 0   1   2   3   4   5   6
[ ] [ ] ... ... ( ) ... [ ]
[S] [S]         [ ]     [ ]
[ ]             [ ]     [ ]
                [S]     [S]


Picosecond 15:
 0   1   2   3   4   5   6
[ ] [ ] ... ... [ ] (.) [ ]
[S] [S]         [ ]     [ ]
[ ]             [ ]     [ ]
                [S]     [S]

 0   1   2   3   4   5   6
[S] [S] ... ... [ ] (.) [ ]
[ ] [ ]         [ ]     [ ]
[ ]             [S]     [S]
                [ ]     [ ]


Picosecond 16:
 0   1   2   3   4   5   6
[S] [S] ... ... [ ] ... ( )
[ ] [ ]         [ ]     [ ]
[ ]             [S]     [S]
                [ ]     [ ]

 0   1   2   3   4   5   6
[ ] [ ] ... ... [ ] ... ( )
[S] [S]         [S]     [S]
[ ]             [ ]     [ ]
                [ ]     [ ]
Because all smaller delays would get you caught, the fewest number of picoseconds you would need to delay to get through safely is 10.

What is the fewest number of picoseconds that you need to delay the packet to pass through the firewall without being caught?
 */

object Day13PacketScanners {

    fun getSeverity(input: String): Int {
        val (scannersRanges, scannersInitials) = initScanners(input)

        return getSeverityTailRec(0, scannersRanges.keys.max()!!, scannersInitials, scannersRanges, 0)
    }

    fun getDelay(input: String): Int {
        val (scannersRanges, scannersInitials) = initScanners(input)

        return getDelayTailRec(0, scannersRanges.keys.max()!!, scannersInitials, scannersInitials, scannersRanges, 0)
    }

    private fun initScanners(input: String): Pair<Map<Int, Int>, Map<Int, Pair<Int, Int>>> {
        val scannersRanges = input.split("\n")
                .fold(linkedMapOf(), { acc: Map<Int, Int>, line ->
                    val layer = line.split(": ")
                    acc.plus(Pair(layer[0].toInt(), layer[1].toInt()))
                })

        val scannersInitials = resetPositions(scannersRanges)
        return Pair(scannersRanges, scannersInitials)
    }

    private tailrec fun getSeverityTailRec(layer: Int, maxLayer: Int, scannersPositions: Map<Int, Pair<Int, Int>>,
                                           scannersRanges: Map<Int, Int>, severity: Int): Int {
        if (layer > maxLayer) return severity

        val currentSeverity = if (scannersPositions[layer]?.first == 1) layer * scannersRanges[layer]!! else 0
        val newScannersPositions = moveScanners(scannersPositions, scannersRanges)

        return getSeverityTailRec(layer + 1, maxLayer, newScannersPositions, scannersRanges, severity + currentSeverity)
    }

    private tailrec fun getDelayTailRec(layer: Int, maxLayer: Int, scannersPositions: Map<Int, Pair<Int, Int>>,
                                        positionsCache: Map<Int, Pair<Int, Int>>, scannersRanges: Map<Int, Int>, delay: Int): Int {
        if (layer > maxLayer) return delay

        if (scannersPositions[layer]?.first == 1) {
            val scannersInitials = moveScanners(positionsCache, scannersRanges)
            return getDelayTailRec(0, maxLayer, scannersInitials, scannersInitials, scannersRanges, delay + 1)
        }

        val newScannersPositions = moveScanners(scannersPositions, scannersRanges)

        return getDelayTailRec(layer + 1, maxLayer, newScannersPositions, positionsCache, scannersRanges, delay)
    }

    private fun resetPositions(scannersPositions: Map<Int, Any>) = scannersPositions.mapValues { Pair(1, 1) }

    private fun moveScanners(scannersPositions: Map<Int, Pair<Int, Int>>, scannersRanges: Map<Int, Int>): Map<Int, Pair<Int, Int>> {
        return scannersPositions.mapValues {
            if (it.value.first < scannersRanges[it.key]!! && it.value.second > 0) Pair(it.value.first + 1, it.value.second)
            else if (it.value.first > 1 && it.value.second < 0) Pair(it.value.first - 1, it.value.second)
            else Pair(it.value.first - it.value.second, -it.value.second)
        }
    }

}

private val inputFileName = "day13.txt"

fun main(args: Array<String>) {
    val input = FileUtil.readFile(inputFileName)

    println("Severity: " + Day13PacketScanners.getSeverity(input))
    println("Delay: " + Day13PacketScanners.getDelay(input))
}
