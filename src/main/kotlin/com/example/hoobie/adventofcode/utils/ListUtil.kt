package com.example.hoobie.adventofcode.utils
 
fun <T> List<T>.patch(idx: Int, value: T): List<T> = this.mapIndexed({ i, v -> if (i == idx) value else v })
