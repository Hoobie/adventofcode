package com.example.hoobie.adventofcode

import spock.lang.Specification

class Day22SporificaVirusSpec extends Specification {

    def "should count infections"() {
        expect:
        new Day22SporificaVirus().countInfections(input, bursts) == result

        where:
        input           || bursts || result
        "..#\n#..\n..." || 7      || 5
        "..#\n#..\n..." || 70     || 41
        "..#\n#..\n..." || 10000  || 5587
    }
}
