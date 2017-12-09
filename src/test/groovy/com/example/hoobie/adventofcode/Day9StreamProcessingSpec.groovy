package com.example.hoobie.adventofcode

import spock.lang.Specification

class Day9StreamProcessingSpec extends Specification {

    def "should calculate the score"() {
        expect:
        new Day9StreamProcessing().calculateScores(input).first == result

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

    def "should count garbage"() {
        expect:
        new Day9StreamProcessing().calculateScores(input).second == result

        where:
        input                 || result
        "<>"                  || 0
        "<random characters>" || 17
        "<<<<>"               || 3
        "<{!>}>"              || 2
        "<!!>"                || 0
        "<!!!>>"              || 0
        "<{o\"i!a,<{i<a>"     || 10
    }

}
