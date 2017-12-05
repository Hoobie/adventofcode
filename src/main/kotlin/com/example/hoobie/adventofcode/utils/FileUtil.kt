package com.example.hoobie.adventofcode.utils

object FileUtil {

    fun readFile(name: String): String {
        return this::class.java.classLoader.getResource(name).readText()
    }

}