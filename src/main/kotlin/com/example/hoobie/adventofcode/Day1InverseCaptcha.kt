package com.example.hoobie.adventofcode

import com.google.common.collect.Streams
import java.util.stream.Stream

/*
--- Day 1: Inverse Captcha ---

The captcha requires you to review a sequence of digits (your puzzle input)
and find the sum of all digits that match the next digit in the list. The list is circular,
so the digit after the last digit is the first digit in the list.

For example:

1122 produces a sum of 3 (1 + 2) because the first digit (1) matches the second digit
and the third digit (2) matches the fourth digit.
1111 produces 4 because each digit (all 1) matches the next.
1234 produces 0 because no digit matches the next.
91212129 produces 9 because the only digit that matches the next one is the last digit, 9.

--- Part Two ---
You notice a progress bar that jumps to 50% completion. Apparently, the door isn't yet satisfied, 
but it did emit a star as encouragement. The instructions change:

Now, instead of considering the next digit, it wants you to consider the digit halfway around the circular list. 
That is, if your list contains 10 items, only include a digit in your sum if the digit 10/2 = 5 steps forward matches it. 
Fortunately, your list has an even number of elements.

For example:

1212 produces 6: the list contains 4 items, and all four digits match the digit 2 items ahead.
1221 produces 0, because every comparison is between a 1 and a 2.
123425 produces 4, because both 2s match each other, but no other digit has a match.
123123 produces 12.
12131415 produces 4.
*/
object Day1InverseCaptcha {

    fun solvePartOne(input: String): Int {
        val first = input.first()
        val last = input.last()
        val result = sumMatches(input.chars().boxed(), input.chars().skip(1).boxed())
        return if (first == last) result + last.parseInt() else result
    }

    fun solvePartTwo(input: String): Int {
        val secondStreamsSecondHalf = input.chars().boxed().limit(input.chars().count() / 2)
        val secondStreamsFirstHalf = input.chars().boxed().skip(input.chars().count() / 2)
        val secondStream = Streams.concat(secondStreamsFirstHalf, secondStreamsSecondHalf)
        return sumMatches(input.chars().boxed(), secondStream)
    }

    private fun sumMatches(firstStream: Stream<Int>, secondStream: Stream<Int>) =
            Streams.zip(firstStream, secondStream, { a, b -> Pair(a, b) })
                    .reduce(0,
                            { sum, p -> if (p.first == p.second) sum + p.first.toChar().parseInt() else sum },
                            Integer::sum)

}

private val input = "95134467996366852979296466896829971143167334454515345323517785342514271721681836218746417115349" +
        "17291674333857423799375512628489423332297538215855176592633692631974822259161766238385922277893623911332569" +
        "44897877194831615586878149669889549297135638399693288551873299762425367869427966657214983161631249799485628" +
        "88715867777934599269524913183369971595537145845418972941174876418726297968255837259756922641258658275346772" +
        "23541484795877371955124463989228886498682421539667224963783616245646832154384756663251487668681425754536722" +
        "82756365132752467418344369622752382883246647353834747299199891321185774987815757917645739537563299557656938" +
        "84558881564654517236937678876813925471892733919486327264998683137472618281867329866283657737285833871841123" +
        "23696592536446536231376615949825166773536471531487969852535699774113163667286537193767515119362865141925612" +
        "84944398348424526819484256315456763835464573533185589615514274166424671566689982436472291429649244467265385" +
        "23873894776342577682297723994165211986253934264434992236118437661348834412233282568834974233247532293923939" +
        "74622181429913535973327323952241674979677481518733692544535323219895684629719868384266425386835539719237716" +
        "33919848516391656243485457936595811193135457699155877123697724266875678213996163834725164482872478682775174" +
        "83991236688543938947878518722566673362157266743488867471282374162731549886192678243612278887515624456223876" +
        "95218161341884756795223464751862965655559143779425283154533252573949165492138175581615176611845489857169132" +
        "93684866864631995566149248842842743526916917365481211484256838163698238922423645563331689817816329745245329" +
        "66676618496221745417786694943881674511863524885553795819349992764129195984114229733993197999375187134223988" +
        "74326665375216437246445791623283898584648278989674418242112957668397484671119761553847275799873495363759266" +
        "29647784415723742323916355939155396117647537715136939964674788145225254774171873494996775256477416134178483" +
        "35214924942436626584711213696496418155623276983952935739916483513697671626427634755615447959821837144477371" +
        "49239846151871434656618825566387329765118727515699213962477996399781652131918996434125559698427945714572488" +
        "376342126989157872118279163127742349"

fun main(args: Array<String>) {
    println("Part One: " + Day1InverseCaptcha.solvePartOne(input))
    println("Part Two: " + Day1InverseCaptcha.solvePartTwo(input))
}

private fun Char.parseInt(): Int {
    return this.toString().toInt()
}
