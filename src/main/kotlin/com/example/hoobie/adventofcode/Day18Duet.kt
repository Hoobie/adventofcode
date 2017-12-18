package com.example.hoobie.adventofcode

import com.example.hoobie.adventofcode.utils.FileUtil

/*
--- Day 18: Duet ---
You discover a tablet containing some strange assembly code labeled simply "Duet". Rather than bother the sound card with it, 
you decide to run the code yourself. Unfortunately, you don't see any documentation, so you're left to figure out 
what the instructions mean on your own.

It seems like the assembly is meant to operate on a set of registers that are each named with a single letter 
and that can each hold a single integer. You suppose each register should start with a value of 0.

There aren't that many instructions, so it shouldn't be hard to figure out what they do. Here's what you determine:

snd X plays a sound with a frequency equal to the value of X.
set X Y sets register X to the value of Y.
add X Y increases register X by the value of Y.
mul X Y sets register X to the result of multiplying the value contained in register X by the value of Y.
mod X Y sets register X to the remainder of dividing the value contained in register X by the value of Y 
(that is, it sets X to the result of X modulo Y).
rcv X recovers the frequency of the last sound played, but only when the value of X is not zero. 
(If it is zero, the command does nothing.)
jgz X Y jumps with an offset of the value of Y, but only if the value of X is greater than zero. 
(An offset of 2 skips the next instruction, an offset of -1 jumps to the previous instruction, and so on.)
Many of the instructions can take either a register (a single letter) or a number. 
The value of a register is the integer it contains; the value of a number is that number.

After each jump instruction, the program continues with the instruction to which the jump jumped. 
After any other instruction, the program continues with the next instruction. 
Continuing (or jumping) off either end of the program terminates it.

For example:

set a 1
add a 2
mul a a
mod a 5
snd a
set a 0
rcv a
jgz a -1
set a 1
jgz a -2
The first four instructions set a to 1, add 2 to it, square it, and then set it to itself modulo 5, resulting in a value of 4.
Then, a sound with frequency 4 (the value of a) is played.
After that, a is set to 0, causing the subsequent rcv and jgz instructions to both be skipped 
(rcv because a is 0, and jgz because a is not greater than 0).
Finally, a is set to 1, causing the next jgz instruction to activate, jumping back two instructions to another jump, 
which jumps again to the rcv, which ultimately triggers the recover operation.
At the time the recover operation is executed, the frequency of the last sound played is 4.

What is the value of the recovered frequency (the value of the most recently played sound) 
the first time a rcv instruction is executed with a non-zero value?

--- Part Two ---
As you congratulate yourself for a job well done, you notice that the documentation has been on the back of the tablet this entire time. 
While you actually got most of the instructions correct, there are a few key differences. 
This assembly code isn't about sound at all - it's meant to be run twice at the same time.

Each running copy of the program has its own set of registers and follows the code independently - in fact, 
the programs don't even necessarily run at the same speed. To coordinate, they use the send (snd) and receive (rcv) instructions:

snd X sends the value of X to the other program. These values wait in a queue until that program is ready to receive them. 
Each program has its own message queue, so a program can never receive a message it sent.
rcv X receives the next value and stores it in register X. If no values are in the queue, the program waits for a value to be sent to it. 
Programs do not continue to the next instruction until they have received a value. Values are received in the order they are sent.
Each program also has its own program ID (one 0 and the other 1); the register p should begin with this value.

For example:

snd 1
snd 2
snd p
rcv a
rcv b
rcv c
rcv d
Both programs begin by sending three values to the other. Program 0 sends 1, 2, 0; program 1 sends 1, 2, 1. 
Then, each program receives a value (both 1) and stores it in a, receives another value (both 2) and stores it in b, 
and then each receives the program ID of the other program (program 0 receives 1; program 1 receives 0) and stores it in c. 
Each program now sees a different value in its own copy of register c.

Finally, both programs try to rcv a fourth time, but no data is waiting for either of them, and they reach a deadlock. 
When this happens, both programs terminate.

It should be noted that it would be equally valid for the programs to run at different speeds; for example, 
program 0 might have sent all three values and then stopped at the first rcv before program 1 executed even its first instruction.

Once both of your programs have terminated (regardless of what caused them to do so), how many times did program 1 send a value?
 */

object Day18Duet {

    private val intRegex = Regex("-?\\d+")
    private val regRegex = Regex("\\w")

    fun getRecoveredFrequency(input: String): Long {
        val instructions: List<Instr> = input.split("\n").map {
            val tokens = it.split(" ")
            when (tokens[0]) {
                "snd" -> Snd(tokens[1])
                "set" -> when (tokens[2]) {
                    in intRegex -> Set(tokens[1], tokens[2].toLong())
                    in regRegex -> SetFromRegister(tokens[1], tokens[2])
                    else -> throw IllegalArgumentException("set")
                }
                "add" -> when (tokens[2]) {
                    in intRegex -> Add(tokens[1], tokens[2].toInt())
                    in regRegex -> AddRegister(tokens[1], tokens[2])
                    else -> throw IllegalArgumentException("add")
                }
                "mul" -> when (tokens[2]) {
                    in intRegex -> Mul(tokens[1], tokens[2].toInt())
                    in regRegex -> MulRegister(tokens[1], tokens[2])
                    else -> throw IllegalArgumentException("mul")
                }
                "mod" -> when (tokens[2]) {
                    in intRegex -> Mod(tokens[1], tokens[2].toInt())
                    in regRegex -> ModRegister(tokens[1], tokens[2])
                    else -> throw IllegalArgumentException("mod")
                }
                "rcv" -> Rcv(tokens[1])
                "jgz" -> {
                    if (tokens[1] in intRegex && tokens[2] in intRegex)
                        Jgz(tokens[1].toLong(), tokens[2].toLong())
                    else if (tokens[1] in regRegex && tokens[2] in intRegex)
                        Jrgz(tokens[1], tokens[2].toLong())
                    else Jrgzr(tokens[1], tokens[2])
                }
                else -> throw IllegalArgumentException("instr")
            }
        }

        val registers = instructions.fold(linkedMapOf(Pair("snd", -1L), Pair("rcv", 0L), Pair("jmp", 0L)), { acc: Map<String, Long>, instr ->
            acc.plus(Pair(instr.destReg, 0L))
        })

        return run(registers, instructions, 0L)
    }

    private tailrec fun run(registers: Map<String, Long>, instructions: List<Instr>, idx: Long): Long {
        if (registers["jmp"] != 0L) {
            return run(registers.plus(Pair("jmp", 0L)), instructions, idx - 1 + registers["jmp"]!!)
        }
        if (idx >= instructions.size || registers["rcv"]!! != 0L) {
            return registers["rcv"]!!
        }
        return run(instructions[idx.toInt()].eval(registers), instructions, idx + 1)
    }

    private operator fun Regex.contains(text: CharSequence): Boolean = this.matches(text)

    private abstract class Instr(open val destReg: String) {
        abstract fun eval(registers: Map<String, Long>): Map<String, Long>
    }

    private data class Snd(val register: String) : Instr(register) {
        override fun eval(registers: Map<String, Long>): Map<String, Long> {
            return registers.plus(Pair("snd", registers[register]!!))
        }
    }

    private data class SetFromRegister(override val destReg: String, val srcReg: String) : Instr(destReg) {
        override fun eval(registers: Map<String, Long>): Map<String, Long> {
            return registers.plus(Pair(destReg, registers[srcReg]!!))
        }
    }

    private data class Set(val register: String, val value: Long) : Instr(register) {
        override fun eval(registers: Map<String, Long>): Map<String, Long> {
            return registers.plus(Pair(register, value))
        }
    }

    private data class AddRegister(override val destReg: String, val srcReg: String) : Instr(destReg) {
        override fun eval(registers: Map<String, Long>): Map<String, Long> {
            return registers.plus(Pair(destReg, registers[destReg]!! + registers[srcReg]!!))
        }
    }

    private data class Add(val register: String, val value: Int) : Instr(register) {
        override fun eval(registers: Map<String, Long>): Map<String, Long> {
            return registers.plus(Pair(register, registers[register]!! + value))
        }
    }

    private data class MulRegister(override val destReg: String, val srcReg: String) : Instr(destReg) {
        override fun eval(registers: Map<String, Long>): Map<String, Long> {
            return registers.plus(Pair(destReg, registers[destReg]!! * registers[srcReg]!!))
        }
    }

    private data class Mul(val register: String, val value: Int) : Instr(register) {
        override fun eval(registers: Map<String, Long>): Map<String, Long> {
            return registers.plus(Pair(register, registers[register]!! * value))
        }
    }

    private data class ModRegister(override val destReg: String, val srcReg: String) : Instr(destReg) {
        override fun eval(registers: Map<String, Long>): Map<String, Long> {
            return registers.plus(Pair(destReg, registers[destReg]!! % registers[srcReg]!!))
        }
    }

    private data class Mod(val register: String, val value: Int) : Instr(register) {
        override fun eval(registers: Map<String, Long>): Map<String, Long> {
            return registers.plus(Pair(register, registers[register]!! % value))
        }
    }

    private data class Rcv(val register: String) : Instr(register) {
        override fun eval(registers: Map<String, Long>): Map<String, Long> {
            return if (registers[register]!! != 0L) registers.plus(Pair("rcv", registers["snd"]!!)) else registers
        }
    }

    private data class Jgz(val val1: Long, val val2: Long) : Instr("jmp") {
        override fun eval(registers: Map<String, Long>): Map<String, Long> {
            return if (val1 > 0) registers.plus(Pair("jmp", val2)) else registers
        }
    }

    private data class Jrgz(val register: String, val value: Long) : Instr(register) {
        override fun eval(registers: Map<String, Long>): Map<String, Long> {
            return if (registers[register]!! > 0) registers.plus(Pair("jmp", value)) else registers
        }
    }

    private data class Jrgzr(val register: String, val srcReg: String) : Instr(register) {
        override fun eval(registers: Map<String, Long>): Map<String, Long> {
            return if (registers[register]!! > 0) registers.plus(Pair("jmp", registers[srcReg]!!)) else registers
        }
    }
}

private val inputFileName = "day18.txt"

fun main(args: Array<String>) {
    val input = FileUtil.readFile(inputFileName)

    println("Received frequency: " + Day18Duet.getRecoveredFrequency(input))
}
