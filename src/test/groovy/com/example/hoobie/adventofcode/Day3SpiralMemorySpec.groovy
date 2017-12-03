package com.example.hoobie.adventofcode

import spock.lang.Specification

class Day3SpiralMemorySpec extends Specification {

    def "should calculate distance"() {
        expect:
        new Day3SpiralMemory().calculateDistance(input) == result

        where:
        input || result
        1     || 0
        12    || 3
        16    || 3
        23    || 2
        1024  || 31
    }

    def "should calculate a first value greater than input"() {
        expect:
        new Day3SpiralMemory().calculateFirstValueGreaterThan(input) == result

        where:
        input || result
//        FIXME
//        1     || 2
//        2     || 4
//        4     || 5
        24    || 25
        330   || 351
        800   || 806
    }

}
