package com.example.hoobie.adventofcode

import spock.lang.Specification

class Day4HighEntropyPassphrasesSpec extends Specification {

    def "should check passphrases one"() {
        expect:
        new Day4HighEntropyPassphrases().countValidPassphrasesOne(input) == result

        where:
        input             || result
        "aa bb cc dd ee"  || 1
        "aa bb cc dd aa"  || 0
        "aa bb cc dd aaa" || 1
    }

    def "should check passphrases two"() {
        expect:
        new Day4HighEntropyPassphrases().countValidPassphrasesTwo(input) == result

        where:
        input                      || result
        "abcde fghij"              || 1
        "abcde xyz ecdab"          || 0
        "a ab abc abd abf abj"     || 1
        "iiii oiii ooii oooi oooo" || 1
        "oiii ioii iioi iiio"      || 0
    }

}
