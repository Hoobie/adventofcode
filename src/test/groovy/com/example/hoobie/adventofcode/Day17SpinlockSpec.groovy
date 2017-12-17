package com.example.hoobie.adventofcode

import spock.lang.Specification

class Day17SpinlockSpec extends Specification {

    def "should insert after"() {
        expect:
        new Day17Spinlock().insertAfter(list, idx, value) == result

        where:
        list               || idx || value || result
        [0]                || 0   || 1     || [0, 1]
        [0, 1]             || 0   || 2     || [0, 2, 1]
        [0, 1, 2, 3, 4, 5] || 0   || 6     || [0, 6, 1, 2, 3, 4, 5]
        [0, 1, 2, 3, 4, 5] || 1   || 6     || [0, 1, 6, 2, 3, 4, 5]
        [0, 1, 2, 3, 4, 5] || 4   || 6     || [0, 1, 2, 3, 4, 6, 5]
        [0, 1, 2, 3, 4, 5] || 5   || 6     || [0, 1, 2, 3, 4, 5, 6]
    }

    def "should get next"() {
        expect:
        new Day17Spinlock().getNext(input, 2017) == result

        where:
        input || result
        "3"   || 638
    }

}
