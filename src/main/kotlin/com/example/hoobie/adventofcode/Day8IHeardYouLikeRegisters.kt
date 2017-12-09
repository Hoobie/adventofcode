package com.example.hoobie.adventofcode

import com.example.hoobie.adventofcode.utils.FileUtil

/*
--- Day 8: I Heard You Like Registers ---

You receive a signal directly from the CPU. Because of your recent assistance with jump instructions, 
it would like you to compute the result of a series of unusual register instructions.

Each instruction consists of several parts: the register to modify, whether to increase or decrease that register's value, 
the amount by which to increase or decrease it, and a condition. If the condition fails, skip the instruction without modifying the register. 
The registers all start at 0. The instructions look like this:

b inc 5 if a > 1
a inc 1 if b < 5
c dec -10 if a >= 1
c inc -20 if c == 10
These instructions would be processed as follows:

Because a starts at 0, it is not greater than 1, and so b is not modified.
a is increased by 1 (to 1) because b is less than 5 (it is 0).
c is decreased by -10 (to 10) because a is now greater than or equal to 1 (it is 1).
c is increased by -20 (to -10) because c is equal to 10.
After this process, the largest value in any register is 1.

You might also encounter <= (less than or equal to) or != (not equal to). However, the CPU doesn't have the bandwidth 
to tell you what all the registers are named, and leaves that to you to determine.

What is the largest value in any register after completing the instructions in your puzzle input?
 */

object Day8IHeardYouLikeRegisters {

    fun findTheLargestValue(input: String): Int {
        input.split("\n").map {
            val tokens = it.split(" ")
            Instruction(Operation(
                    tokens[0],
                    when (tokens[1]) {
                        Inc.toString() -> Inc
                        Dec.toString() -> Dec
                        else -> throw IllegalArgumentException()
                    },
                    tokens[2].toInt()
            ), Condition(
                    tokens[4],
                    when (tokens[5]) {
                        Eq.toString() -> Eq
                        Ne.toString() -> Ne
                        Gt.toString() -> Gt
                        Gte.toString() -> Gte
                        Lt.toString() -> Lt
                        Lte.toString() -> Lte
                        else -> throw IllegalArgumentException()
                    },
                    tokens[6].toInt()
            ))
        }.fold(hashMapOf(), { acc: Map<String, Int>, ins -> ins.eval(acc) })

        return 0
    }

}

data class Instruction(private val operation: Operation, private val condition: Condition) {
    fun eval(registers: Map<String, Int>): Map<String, Int> {
        val newRegisters = initializeRegister(registers)
        return if (condition.eval(newRegisters))
            newRegisters.plus(Pair(operation.register, operation.eval(newRegisters))) else newRegisters
    }

    private fun initializeRegister(registers: Map<String, Int>): Map<String, Int> {
        val new = if (!registers.contains(operation.register))
            registers.plus(Pair(operation.register, 0)) else registers
        return if (!new.contains(condition.register))
            new.plus(Pair(condition.register, 0)) else new
    }
}

data class Operation(val register: String, private val operation: OperationType, private val value: Int) {
    fun eval(registers: Map<String, Int>): Int {
        return when (operation) {
            Inc -> registers[register]!! + value
            Dec -> registers[register]!! - value
        }
    }
}

sealed class OperationType
object Inc : OperationType() {
    override fun toString(): String {
        return "inc"
    }
}

object Dec : OperationType() {
    override fun toString(): String {
        return "dec"
    }
}

data class Condition(val register: String, private val comparison: ComparisonType, private val value: Int) {
    fun eval(registers: Map<String, Int>): Boolean {
        return when (comparison) {
            Eq -> registers[register] == value
            Ne -> registers[register] != value
            Gt -> registers[register]!! > value
            Gte -> registers[register]!! >= value
            Lt -> registers[register]!! < value
            Lte -> registers[register]!! >= value
        }
    }
}

sealed class ComparisonType
object Eq : ComparisonType() {
    override fun toString(): String {
        return "=="
    }
}

object Ne : ComparisonType() {
    override fun toString(): String {
        return "!="
    }
}

object Gt : ComparisonType() {
    override fun toString(): String {
        return ">"
    }
}

object Gte : ComparisonType() {
    override fun toString(): String {
        return ">="
    }
}

object Lt : ComparisonType() {
    override fun toString(): String {
        return "<"
    }
}

object Lte : ComparisonType() {
    override fun toString(): String {
        return "<="
    }
}

private val inputFileName = "day8.txt"

fun main(args: Array<String>) {
    val input = FileUtil.readFile(inputFileName)

    println("Largest value: " + Day8IHeardYouLikeRegisters.findTheLargestValue(input))
}