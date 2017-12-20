package com.example.hoobie.adventofcode.day18

val intRegex = Regex("-?\\d+")
val regRegex = Regex("\\w")

operator fun Regex.contains(text: CharSequence): Boolean = this.matches(text)
