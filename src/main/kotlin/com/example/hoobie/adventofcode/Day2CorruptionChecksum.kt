package com.example.hoobie.adventofcode

/*
--- Day 2: Corruption Checksum ---

As you walk through the door, a glowing humanoid shape yells in your direction. "You there! Your state appears to be idle. Come help us repair the corruption in this spreadsheet - if we take another millisecond, we'll have to display an hourglass cursor!"

The spreadsheet consists of rows of apparently-random numbers. To make sure the recovery process is on the right track, they need you to calculate the spreadsheet's checksum. For each row, determine the difference between the largest value and the smallest value; the checksum is the sum of all of these differences.

For example, given the following spreadsheet:

5 1 9 5
7 5 3
2 4 6 8
The first row's largest and smallest values are 9 and 1, and their difference is 8.
The second row's largest and smallest values are 7 and 3, and their difference is 4.
The third row's difference is 6.
In this example, the spreadsheet's checksum would be 8 + 4 + 6 = 18.

--- Part Two ---

"Great work; looks like we're on the right track after all. Here's a star for your effort." However, the program seems a little worried. Can programs be worried?

"Based on what we're seeing, it looks like all the User wanted is some information about the evenly divisible values in the spreadsheet. Unfortunately, none of us are equipped for that kind of calculation - most of us specialize in bitwise operations."

It sounds like the goal is to find the only two numbers in each row where one evenly divides the other - that is, where the result of the division operation is a whole number. They would like you to find those numbers on each line, divide them, and add up each line's result.

For example, given the following spreadsheet:

5 9 2 8
9 4 7 3
3 8 6 5
In the first row, the only two numbers that evenly divide are 8 and 2; the result of this division is 4.
In the second row, the two numbers are 9 and 3; the result is 3.
In the third row, the result is 2.
In this example, the sum of the results would be 4 + 3 + 2 = 9.
 */

object Day2CorruptionChecksum {

    fun calculateChecksum(input: String): Int {
        return mapAndSum(input, {
            val ints = splitRow(it)
            val min = ints.min() ?: 0
            val max = ints.max() ?: 0
            max - min
        })
    }

    fun sumEvenlyDivisibleValues(input: String): Int {
        return mapAndSum(input, {
            val ints = splitRow(it)
            val pair = ints.flatMap { a -> ints.map { b -> Pair(a, b) } }
                    .find { it.first % it.second == 0 && it.first != it.second } ?: Pair(0, 1)
            pair.first / pair.second
        })
    }

    private fun mapAndSum(input: String, mapper: (c: String) -> Int): Int {
        return input.split("\n").map(mapper).sum()
    }

    private fun splitRow(row: String) = row.split("\t").map { it.toInt() }

}

private val input = """414	382	1515	319	83	1327	116	391	101	749	1388	1046	1427	105	1341	1590
960	930	192	147	932	621	1139	198	865	820	597	165	232	417	19	183
3379	987	190	3844	1245	1503	3151	3349	2844	4033	175	3625	3565	179	3938	184
116	51	32	155	102	92	65	42	48	91	74	69	52	89	20	143
221	781	819	121	821	839	95	117	626	127	559	803	779	543	44	369
199	2556	93	1101	122	124	2714	625	2432	1839	2700	2636	118	2306	1616	2799
56	804	52	881	1409	47	246	1368	1371	583	49	1352	976	400	1276	1240
1189	73	148	1089	93	76	3205	3440	3627	92	853	95	3314	3551	2929	3626
702	169	492	167	712	488	357	414	187	278	87	150	19	818	178	686
140	2220	1961	1014	2204	2173	1513	2225	443	123	148	580	833	1473	137	245
662	213	1234	199	1353	1358	1408	235	917	1395	1347	194	565	179	768	650
119	137	1908	1324	1085	661	1557	1661	1828	1865	432	110	658	821	1740	145
1594	222	4140	963	209	2782	180	2591	4390	244	4328	3748	4535	192	157	3817
334	275	395	128	347	118	353	281	430	156	312	386	160	194	63	141
146	1116	153	815	2212	2070	599	3018	2640	47	125	2292	165	2348	2694	184
1704	2194	1753	146	2063	1668	1280	615	163	190	2269	1856	150	158	2250	2459"""

fun main(args: Array<String>) {
    println("Checksum: " + Day2CorruptionChecksum.calculateChecksum(input))
    println("Sum of evenly divisible values: " + Day2CorruptionChecksum.sumEvenlyDivisibleValues(input))
}
