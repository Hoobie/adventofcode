package com.example.hoobie.adventofcode

import spock.lang.Specification

class Day14DiskDefragmentationSpec extends Specification {

    def "should count used squares"() {
        expect:
        new Day14DiskDefragmentation().countUsedSquares(input) == result

        where:
        input      || result
        "flqrgnkx" || 8108
    }

    def "should count regions"() {
        expect:
        new Day14DiskDefragmentation().countRegions(input) == result

        where:
        input      || result
        "flqrgnkx" || 1242
    }

}
