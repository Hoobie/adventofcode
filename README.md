# Advent of Code: Were you nice or naughty this year?

## What's going on?
Advent of Code is an annual programming challenge which consists of 25 (2-part) puzzles at different difficulty levels.
I heard about it on our company's Slack and it's the first edition I participated. The event itself is a great opportunity to learn 
a new programming language (I've got familiar with Kotlin) and to compare your solutions with friends.
This year's plot was about the Santa's broken printer. We were shrunken and sent inside the computer to fix it in 25 ~~days~~ milliseconds.

## Some stats
The first puzzle was finished by over 37 000 people. Each day this number was decreasing despite the 4th, 8th, 15th and 22nd day,
where the previous days were visibly harder and skipped by a lot of people. E.g. the fastest solution of the 21st task was
10 minutes 9 seconds in opposite to the next day's 6 minutes 2 seconds. The hardest task overall seems to be the 23rd day's second part
which was tackled by the top user in above 19 minutes. This puzzle required to optimize the assembly-like code instead of just computing a result.
Finally, just over 3000 competitors survived the whole 25 days.

![stats](stats.png)

## An example solution (spoiler alert)
This paragraph presents an example functional solution of the 21st day's "Fractal Art" exercise which was one of the toughest.

### Intro
We start with the following ASCII art:
```
.#.
..#
###
```
The pattern is a 3x3 square, so it has a size of 3.

Then, we repeat this process:
* If the size of the image is evenly divisible by 2, break it into 2x2 squares, and convert each 2x2 square into a 3x3 square 
with the corresponding rule from the artist's book of enhancement rules.
* Otherwise, the size is evenly divisible by 3; break the image into 3x3 squares, and convert each 3x3 square into a 4x4 square 
by following the corresponding rule.

As we can see, the image increases its size.

The book of rules (our puzzle input) is incomplete. So, we have to rotate or flip the input pattern to find a match.
For example, the book may look like this:
```
../.# => ##./#../...
.#./..#/### => #..#/..../..../#..#
```
Pattern's rows are separated by slashes.

The puzzle was to count turned on pixels (`#`) after n iterations.

The whole content is available on: http://adventofcode.com/2017/day/21

### The main part
We have to repeat the process of:
* dividing the image,
* transforming it in accordance to the book of rules,
* merging the result.

So, the main part of the solution looks like this:
```kotlin
val image = (0 until iterations).fold(startPattern, { acc, _ ->
    val divided = divide(acc)

    val transformed = divided.map {
        it.map { transform(it, rules) }
    }

    merge(transformed)
})
```
where `rules` is a `Map<String, String>`.

### Dividing
Suppose we have the following `divide` function:
```kotlin
fun divide(pattern: String): List<List<String>> {
    val lines = pattern.split("/")
    val size = lines[0].length

    if (size == 2 || size == 3) return listOf(listOf(pattern))

    return if (size % 2 == 0 && size > 2)
        divide(lines, 2)
    else if (size % 3 == 0 && size > 3)
        divide(lines, 3)
    else throw IllegalArgumentException("divide")
}
```
which does not require further explanations and is an entry point for the proper divide function:
```kotlin
fun divide(lines: List<String>, divider: Int): List<List<String>> =
    lines
        .map { it.chunked(divider) }.chunked(divider)
        .map { groupByIndex(it) }
        .map { it.map { it.joinToString("/") } }
```
The first map uses the Kotlin's feature which produces a list of chunks with specified size.
This is made twice as we want to divide columns and rows as well.
Then, we need to group our pixels by indexes in chunks to produce proper patterns.
Finally, we have to merge rows together.
You can see better how it works by looking at a [Spock](http://spockframework.org/) specification for the `divide` function:
```groovy
def "should divide"() {
    expect:
    new Day21FractalArt().divide(input) == result

    where:
    input                                       || result
    "#..#/..../..../#..#"                       || [["#./..", ".#/.."], ["../#.", "../.#"]]
    "#...../.#..../..#.../...#../....#./.....#" || [["#./.#", "../..", "../.."], ["../..", "#./.#", "../.."], ["../..", "../..", "#./.#"]]
}
```

### Transforming
The tail recursive `transform` function takes a pattern, a book of rules and a counter of rotations.
After 4 rotations a pattern backs to the starting position, so we throw an exception.
Then the series of `if` statements checks if there is a corresponding rule for the one of the possible combinations of positions.
```kotlin
tailrec fun transform(pattern: String, rules: Map<String, String>, rotateCounter: Int = 0): String {
    if (rotateCounter > 3) throw IllegalArgumentException("transform")

    if (pattern in rules) return rules[pattern]!!

    val horizontalFlip = flipHorizontally(pattern)
    if (horizontalFlip in rules) return rules[horizontalFlip]!!
    if (flipVertically(pattern) in rules) return rules[flipVertically(pattern)]!!
    if (flipVertically(horizontalFlip) in rules) return rules[flipVertically(horizontalFlip)]!!

    return transform(rotate(pattern), rules, rotateCounter + 1)
}
```
To see how the `flip` and `rotate` functions are implemented, visit my GitHub.

### Merging
The `merge` function, first, splits rows by slashes, then, groups pixels with the same function as in the `dividing` phase.
Finally, it merges all pixels together.
```kotlin
fun merge(dividedPattern: List<List<String>>): String =
    dividedPattern.map { it.map { it.split("/") } }
        .map { groupByIndex(it) }
        .joinToString("/") {
            it.joinToString("/") {
                it.joinToString("")
            }
        }
```
An example test should clarify how it works:
```groovy
def "should merge"() {
    expect:
    new Day21FractalArt().merge(input) == result

    where:
    input                                    || result
    [["#./..", ".#/.."], ["../#.", "../.#"]] || "#..#/..../..../#..#"
}
```

### Last few words
All my solutions can be found on my GitHub: https://github.com/Hoobie/adventofcode
They are written in Kotlin and almost all of them are [functional](https://en.wikipedia.org/wiki/Functional_programming).
