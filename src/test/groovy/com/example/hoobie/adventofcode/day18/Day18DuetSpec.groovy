package com.example.hoobie.adventofcode.day18

import spock.lang.Specification

class Day18DuetSpec extends Specification {

    def "should get recovered frequency"() {
        expect:
        new Day18DuetPart1().getRecoveredFrequency(input) == result

        where:
        input                                                                                    || result
        "set a 1\nadd a 2\nmul a a\nmod a 5\nsnd a\nset a 0\nrcv a\njgz a -1\nset a 1\njgz a -2" || 4
    }

    def "should count the second program's sends"() {
        expect:
        new Day18DuetPart2().countSecondProgramsSends(input) == result

        where:
        input                                             || result
        "snd 1\nsnd 2\nsnd p\nrcv a\nrcv b\nrcv c\nrcv d" || 3
    }

}
