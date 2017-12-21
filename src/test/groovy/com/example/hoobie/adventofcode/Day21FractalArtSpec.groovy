package com.example.hoobie.adventofcode

import spock.lang.Specification

class Day21FractalArtSpec extends Specification {

    def "should divide"() {
        expect:
        new Day21FractalArt().divide(input) == result

        where:
        input                                       || result
        "#..#/..../..../#..#"                       || [["#./..", ".#/.."], ["../#.", "../.#"]]
        "#...../.#..../..#.../...#../....#./.....#" || [["#../.#./..#", ".../.../..."], [".../.../...", "#../.#./..#"]]
    }

}
