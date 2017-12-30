package com.example.hoobie.adventofcode

import com.example.hoobie.adventofcode.common.*
import com.example.hoobie.adventofcode.common.Set
import com.example.hoobie.adventofcode.utils.FileUtil

/*
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

object Day18DuetPart2 {

    fun countSecondProgramsSends(input: String): Long {
        val instructions: List<Instruction> = input.split("\n").map {
            val tokens = it.split(" ")
            when (tokens[0]) {
                "snd" -> when (tokens[1]) {
                    in intRegex -> Send(tokens[1].toLong())
                    in letterRegex -> SendRegister(tokens[1])
                    else -> throw IllegalArgumentException("snd")
                }
                "set" -> when (tokens[2]) {
                    in intRegex -> Set(tokens[1], tokens[2].toLong())
                    in letterRegex -> SetFromRegister(tokens[1], tokens[2])
                    else -> throw IllegalArgumentException("set")
                }
                "add" -> when (tokens[2]) {
                    in intRegex -> Add(tokens[1], tokens[2].toInt())
                    in letterRegex -> AddRegister(tokens[1], tokens[2])
                    else -> throw IllegalArgumentException("add")
                }
                "mul" -> when (tokens[2]) {
                    in intRegex -> Mul(tokens[1], tokens[2].toInt())
                    in letterRegex -> MulRegister(tokens[1], tokens[2])
                    else -> throw IllegalArgumentException("mul")
                }
                "mod" -> when (tokens[2]) {
                    in intRegex -> Mod(tokens[1], tokens[2].toInt())
                    in letterRegex -> ModRegister(tokens[1], tokens[2])
                    else -> throw IllegalArgumentException("mod")
                }
                "rcv" -> Receive(tokens[1])
                "jgz" -> {
                    if (tokens[1] in intRegex && tokens[2] in intRegex)
                        Jgz(tokens[1].toLong(), tokens[2].toLong())
                    else if (tokens[1] in letterRegex && tokens[2] in intRegex)
                        Jrgz(tokens[1], tokens[2].toLong())
                    else Jrgzr(tokens[1], tokens[2])
                }
                else -> throw IllegalArgumentException("instr")
            }
        }

        val registers = instructions.fold(linkedMapOf(Pair("snd", 0L), Pair("rcv", 0L), Pair("jmp", 0L)),
                { acc: Map<String, Long>, instr ->
                    acc.plus(Pair(instr.destReg, 0L))
                })

        val program1 = Program(0, registers, instructions)
        val program2 = Program(1, registers.plus(Pair("p", 1L)), instructions)

        return run(program1, program2)
    }

    private tailrec fun run(p1: Program, p2: Program): Long {
        if (p1.counter >= p1.instructions.size || p2.counter >= p2.instructions.size
                || ((p1.instructions[p1.counter] is Receive && p1.queue.isEmpty())
                && (p2.instructions[p2.counter] is Receive && p2.queue.isEmpty()))) {
            return if (p1.id == 1) p1.sendCounter else p2.sendCounter
        }

        if (p1.regs["jmp"] != 0L) { // TODO: refactor
            return run(p1.copy(regs = p1.regs.plus(Pair("jmp", 0L)),
                    counter = (p1.counter - 1 + p1.regs["jmp"]!!).toInt()), p2)
        }

        if (p1.instructions[p1.counter] is Send || p1.instructions[p1.counter] is SendRegister) { // TODO: refactor
            val newRegs = p1.instructions[p1.counter].eval(p1.regs)
            return run(p1.copy(regs = newRegs.plus(Pair("snd", 0L)), counter = p1.counter + 1, sendCounter = p1.sendCounter + 1),
                    p2.copy(queue = p2.queue.plus(newRegs["snd"]!!)))
        }

        if (p1.regs["rcv"] != 0L && p1.queue.isEmpty()) { // TODO: refactor
            return run(p2, p1) // switch context
        } else if (p1.regs["rcv"] != 0L) {
            val newRegs = p1.regs.plus(Pair(p1.regs["rcv"]!!.toInt().toChar().toString(), p1.queue.first())).plus(Pair("rcv", 0L))
            return run(p1.copy(regs = newRegs, queue = p1.queue.drop(1)),
                    p2)
        }

        return run(p1.copy(regs = p1.instructions[p1.counter].eval(p1.regs), counter = p1.counter + 1), p2)
    }

    private data class Program(val id: Int,
                               val regs: Map<String, Long>,
                               val instructions: List<Instruction>,
                               val counter: Int = 0,
                               val queue: List<Long> = listOf(),
                               val sendCounter: Long = 0L)

}

private val inputFileName = "day18.txt"

fun main(args: Array<String>) {
    val input = FileUtil.readFile(inputFileName)

    println("Programs: " + Day18DuetPart2.countSecondProgramsSends(input))
}
