package com.example.hoobie.adventofcode

import spock.lang.Specification

class Day5AMazeOfTwistyTrampolinesSpec extends Specification {

    def "should count steps one"() {
        expect:
        new Day5AMazeOfTwistyTrampolines().countStepsToReachTheExit(input, false) == result

        where:
        input            || result
        "0\n3\n0\n1\n-3" || 5
    }

    def "should count steps two"() {
        expect:
        new Day5AMazeOfTwistyTrampolines().countStepsToReachTheExit(input, true) == result

        where:
        input            || result
        "0\n3\n0\n1\n-3" || 10
    }

}
