package com.example.hoobie.adventofcode

import spock.lang.Specification

class Day8IHeardYouLikeRegistersSpec extends Specification {

    def "should find the largest value"() {
        expect:
        new Day8IHeardYouLikeRegisters().findTheLargestValues(input).first == result

        where:
        input                                                                           || result
        "b inc 5 if a > 1\na inc 1 if b < 5\nc dec -10 if a >= 1\nc inc -20 if c == 10" || 1
    }

    def "should find the largest value ever"() {
        expect:
        new Day8IHeardYouLikeRegisters().findTheLargestValues(input).second == result

        where:
        input                                                                           || result
        "b inc 5 if a > 1\na inc 1 if b < 5\nc dec -10 if a >= 1\nc inc -20 if c == 10" || 10
    }

}
