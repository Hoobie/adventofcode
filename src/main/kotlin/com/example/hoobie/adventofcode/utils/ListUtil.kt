package com.example.hoobie.adventofcode.utils

fun <T> List<T>.patch(idx: Int, value: T): List<T> = this.mapIndexed({ i, v -> if (i == idx) value else v })

fun <T> List<List<T>>.patch(x: Int, y: Int, value: T): List<List<T>> = this.patch(y, this[y].patch(x, value))
