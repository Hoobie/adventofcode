package com.example.hoobie.adventofcode

import spock.lang.Specification

class Day10KnotHashSpec extends Specification {

    def "should swap"() {
        given:
        def l = new IntCircularList()
        l += 0..4

        when:
        l.swap(6, 8)

        then:
        l == [0, 3, 2, 1, 4]
    }

    def "should multiply"() {
        expect:
        def list = new Day10KnotHash().hashAndMultiplyTheFirstTwoNumbers(input, 0, 4, 0, 0, new IntCircularList(), null).first
        list[0] * list[1] == result

        where:
        input     || result
        "3,4,1,5" || 12
    }

    def "should hash"() {
        expect:
        new Day10KnotHash().knotHash(input) == result

        where:
        input      || result
        ""         || "a2582a3a0e66e6e86e3812dcb672a272"
        "AoC 2017" || "33efeb34ea91902bb2f59c9920caa6cd"
        "1,2,3"    || "3efbe78a8d82f29979031a4aa0b16a9d"
        "1,2,4"    || "63960835bcdc130f0b66d7ff4f6a5a8e"
    }

}
