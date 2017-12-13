package com.example.hoobie.adventofcode

import spock.lang.Specification

class Day13PacketScannersSpec extends Specification {

    def "should get severity"() {
        expect:
        new Day13PacketScanners().getSeverity(input) == result

        where:
        input                    || result
        "0: 3\n1: 2\n4: 4\n6: 4" || 24
    }

    def "should get delay"() {
        expect:
        new Day13PacketScanners().getDelay(input) == result

        where:
        input                    || result
        "0: 3\n1: 2\n4: 4\n6: 4" || 10
    }

}
