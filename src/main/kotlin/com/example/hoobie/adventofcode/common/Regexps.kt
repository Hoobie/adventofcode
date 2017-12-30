package com.example.hoobie.adventofcode.common

val intRegex = Regex("-?\\d+")
val letterRegex = Regex("\\w")

operator fun Regex.contains(text: CharSequence): Boolean = this.matches(text)
