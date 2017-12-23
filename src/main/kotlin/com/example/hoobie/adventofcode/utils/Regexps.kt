package com.example.hoobie.adventofcode.utils

val intRegex = Regex("-?\\d+")
val regRegex = Regex("\\w")

operator fun Regex.contains(text: CharSequence): Boolean = this.matches(text)
