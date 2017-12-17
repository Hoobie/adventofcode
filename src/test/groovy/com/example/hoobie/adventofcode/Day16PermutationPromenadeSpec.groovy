package com.example.hoobie.adventofcode

import spock.lang.Specification

class Day16PermutationPromenadeSpec extends Specification {

    def "should spin"() {
        expect:
        new Day16PermutationPromenade().spin(["a", "b", "c"], x) == result

        where:
        x || result
        0 || ["a", "b", "c"]
        1 || ["c", "a", "b"]
        2 || ["b", "c", "a"]
        3 || ["a", "b", "c"]
    }

    def "should swap"() {
        expect:
        new Day16PermutationPromenade().swap(["a", "b", "c"], elem1, elem2) == result

        where:
        elem1 || elem2 || result
        "b"   || "c"   || ["a", "c", "b"]
        "c"   || "b"   || ["a", "c", "b"]
        1     || 2     || ["a", "c", "b"]
        2     || 1     || ["a", "c", "b"]
        1     || 1     || ["a", "b", "c"]
        "b"   || "b"   || ["a", "b", "c"]
        "a"   || "c"   || ["c", "b", "a"]
        "c"   || "a"   || ["c", "b", "a"]
        0     || 2     || ["c", "b", "a"]
        2     || 0     || ["c", "b", "a"]
        "a"   || "a"   || ["a", "b", "c"]
        "c"   || "c"   || ["a", "b", "c"]
    }

    def "should swap advanced"() {
        expect:
        new Day16PermutationPromenade().swap(["a", "b", "c", "d", "e"], elem1, elem2) == result

        where:
        elem1 || elem2 || result
        "a"   || "e"   || ["e", "b", "c", "d", "a"]
        "e"   || "a"   || ["e", "b", "c", "d", "a"]
        0     || 4     || ["e", "b", "c", "d", "a"]
        4     || 0     || ["e", "b", "c", "d", "a"]
        "d"   || "c"   || ["a", "b", "d", "c", "e"]
        "b"   || "d"   || ["a", "d", "c", "b", "e"]
        3     || 2     || ["a", "b", "d", "c", "e"]
        1     || 3     || ["a", "d", "c", "b", "e"]
    }

    def "should get order"() {
        expect:
        new Day16PermutationPromenade().getOrder(["a", "b", "c", "d", "e"], input) == result

        where:
        input          || result
        "s1,x3/4,pe/b" || "baedc"
    }

}
