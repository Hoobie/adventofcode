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
        new Day10KnotHash().hashAndMultiplyTheFirstTwoNumbers(input, 0, 4) == result

        where:
        input     || result
        "3,4,1,5" || 12
    }

}
