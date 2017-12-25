package com.example.hoobie.adventofcode

import spock.lang.Specification

class Day25TheHaltingProblemSpec extends Specification {

    def "should create zeros"() {
        expect:
        new Day25TheHaltingProblem().zeros(5) == result

        where:
        input || result
        5     || [0.byteValue(), 0.byteValue(), 0.byteValue(), 0.byteValue(), 0.byteValue()]
    }
}
