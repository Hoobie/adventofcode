package com.example.hoobie.adventofcode

import spock.lang.Specification

class Day21FractalArtSpec extends Specification {

    def "should divide"() {
        expect:
        new Day21FractalArt().divide(input) == result

        where:
        input                                       || result
        "#..#/..../..../#..#"                       || [["#./..", ".#/.."], ["../#.", "../.#"]]
        "#...../.#..../..#.../...#../....#./.....#" || [["#./.#", "../..", "../.."], ["../..", "#./.#", "../.."], ["../..", "../..", "#./.#"]]
    }

    def "should flip horizontally"() {
        expect:
        new Day21FractalArt().flipHorizontally(input) == result

        where:
        input         || result
        "#../##./#.." || "..#/.##/..#"
    }

    def "should flip vertically"() {
        expect:
        new Day21FractalArt().flipVertically(input) == result

        where:
        input         || result
        "###/.#./..." || ".../.#./###"
    }

    def "should rotate"() {
        expect:
        new Day21FractalArt().rotate(input) == result

        where:
        input         || result

        "###/.../..." || "#../#../#.."

        ".#./..#/###" || ".##/#.#/..#"
        ".##/#.#/..#" || "###/#../.#."
        "###/#../.#." || "#../#.#/##."
    }

    def "should merge"() {
        expect:
        new Day21FractalArt().merge(input) == result

        where:
        input                                    || result
        [["#./..", ".#/.."], ["../#.", "../.#"]] || "#..#/..../..../#..#"
    }
}
