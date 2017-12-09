package com.example.hoobie.adventofcode

import spock.lang.Specification

class Day9StreamProcessingSpec extends Specification {

    def "should calculate the score"() {
        expect:
        new Day9StreamProcessing().calculateTheScore(input) == result

        where:
        input                           || result
        "{}"                            || 1
        "{{{}}}"                        || 6
        "{{},{}}"                       || 5
        "{{{},{},{{}}}}"                || 16
        "{<a>,<a>,<a>,<a>}"             || 1
        "{{<ab>},{<ab>},{<ab>},{<ab>}}" || 9
        "{{<!!>},{<!!>},{<!!>},{<!!>}}" || 9
        "{{<a!>},{<a!>},{<a!>},{<ab>}}" || 3
    }

}
