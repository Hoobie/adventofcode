package com.example.hoobie.adventofcode

import spock.lang.Specification

class Day6MemoryReallocationSpec extends Specification {

    def "should count cycles"() {
        expect:
        new Day6MemoryReallocation().countCyclesAndLoopSize(input).first == result

        where:
        input        || result
        "0\t2\t7\t0" || 5
    }

    def "should calculate the size of the loop"() {
        expect:
        new Day6MemoryReallocation().countCyclesAndLoopSize(input).second == result

        where:
        input        || result
        "0\t2\t7\t0" || 4
    }

}
