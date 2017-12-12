package com.example.hoobie.adventofcode

import spock.lang.Specification

class Day12DigitalPlumberSpec extends Specification {

    def "should count programs"() {
        expect:
        new Day12DigitalPlumber().countGroups(input).first == result

        where:
        input                                                                             || result
        "0 <-> 2\n1 <-> 1\n2 <-> 0, 3, 4\n3 <-> 2, 4\n4 <-> 2, 3, 6\n5 <-> 6\n6 <-> 4, 5" || 6
    }

    def "should count groups"() {
        expect:
        new Day12DigitalPlumber().countGroups(input).second == result

        where:
        input                                                                             || result
        "0 <-> 2\n1 <-> 1\n2 <-> 0, 3, 4\n3 <-> 2, 4\n4 <-> 2, 3, 6\n5 <-> 6\n6 <-> 4, 5" || 2
    }

}
