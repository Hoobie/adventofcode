package com.example.hoobie.adventofcode

import spock.lang.Specification

class Day18DuetSpec extends Specification {

    def "should get recovered frequency"() {
        expect:
        new Day18Duet().getRecoveredFrequency(input) == result

        where:
        input                                                                                    || result
        "set a 1\nadd a 2\nmul a a\nmod a 5\nsnd a\nset a 0\nrcv a\njgz a -1\nset a 1\njgz a -2" || 4
    }
    
}
