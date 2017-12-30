package com.example.hoobie.adventofcode

/*
set b 65
set c b
jnz a 2
jnz 1 5
mul b 100
add b 100000
set c b
add c 17000
set f 1 // @loop23
set d 2
set e 2         // @loop13
set g d             // @loop8
mul g e
sub g b
jnz g 2             // if g == 0
set f 0
add e 1
set g e
sub g b
jnz g -8            // @loop8
add d 1
set g d
sub g b
jnz g -13       // @loop13
jnz f 2
add h 1
set g b
sub g c
jnz g 2
jnz 1 3
add b 17
jnz 1 -23 // @loop23
 */

fun main(args: Array<String>) {
    var b = 106500
    val c = b + 17000
    var g = 0
    var h = 0
//    while (true) {
//        var f = 1
//        var d = 2
//        do {
//            var e = 2
//            do {
//                g = d * e - b
//                if (g == 0) f = 0
//                e += 1
//                g = e - b
//            } while (g != 0)
//            d += 1
//            g = d - b
//        } while (g != 0)
//        if (f == 0) h += 1
//        g = b - c
//        if (g == 0) {
//            println(h)
//            return
//        }
//        b += 17
//    }

    (b..c + 1 step 17)
            .filterNot { isPrime(it) }
            .forEach { h += 1 }

    println(h)
}

fun isPrime(num: Int): Boolean {
    return !(2..num / 2).any {
        num % it == 0
    }
}