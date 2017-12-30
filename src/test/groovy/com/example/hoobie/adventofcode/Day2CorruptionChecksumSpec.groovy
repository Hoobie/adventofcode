package com.example.hoobie.adventofcode

import spock.lang.Specification

class Day2CorruptionChecksumSpec extends Specification {

    def "should calculate checksum"() {
        expect:
        new Day2CorruptionChecksum().calculateChecksum(input) == result

        where:
        input                             || result
        "5\t1\t9\t5\n7\t5\t3\n2\t4\t6\t8" || 18
    }

    def "should sum evenly divisible values"() {
        expect:
        new Day2CorruptionChecksum().sumEvenlyDivisibleValues(input) == result

        where:
        input                                || result
        "5\t9\t2\t8\n9\t4\t7\t3\n3\t8\t6\t5" || 9
    }

}
