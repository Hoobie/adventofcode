package com.example.hoobie.adventofcode

import spock.lang.Specification

class Day11HexEdSpec extends Specification {

    def "should compute steps"() {
        expect:
        new Day11HexEd().computeSteps(input).first == result

        where:
        input                 || result
        "ne,ne,ne"            || 3
        "nw,nw,n"             || 3
        "ne,ne,sw,sw"         || 0
        "ne,ne,s,s"           || 2
        "nw,nw,s,s"           || 2
        "sw,sw,n,n"           || 2
        "se,se,n,n"           || 2
        "se,sw,se,sw,sw"      || 3
        "ne,ne,ne,nw,nw"      || 3
        "ne,ne,s,s,nw,nw,s,s" || 2
        "sw,sw,n,n,se,se,n,n" || 2
    }

}
