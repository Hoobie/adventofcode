package com.example.hoobie.adventofcode

import spock.lang.Specification

class Day1InverseCaptchaSpec extends Specification {

    def "should solve part one"() {
        expect:
        new Day1InverseCaptcha().solvePartOne(input) == result

        where:
        input      || result
        "1122"     || 3
        "1111"     || 4
        "1234"     || 0
        "91212129" || 9
    }

    def "should solve part two"() {
        expect:
        new Day1InverseCaptcha().solvePartTwo(input) == result

        where:
        input      || result
        "1212"     || 6
        "1221"     || 0
        "123425"   || 4
        "123123"   || 12
        "12131415" || 4
    }

}
