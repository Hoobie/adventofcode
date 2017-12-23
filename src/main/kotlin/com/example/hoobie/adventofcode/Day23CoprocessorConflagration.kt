package com.example.hoobie.adventofcode

import com.example.hoobie.adventofcode.utils.*
import com.example.hoobie.adventofcode.utils.Set

/*
You decide to head directly to the CPU and fix the printer from there. As you get close, 
you find an experimental coprocessor doing so much work that the local programs are afraid it will halt and catch fire. 
This would cause serious issues for the rest of the computer, so you head in and see what you can do.

The code it's running seems to be a variant of the kind you saw recently on that tablet. 
The general functionality seems very similar, but some of the instructions are different:

set X Y sets register X to the value of Y.
sub X Y decreases register X by the value of Y.
mul X Y sets register X to the result of multiplying the value contained in register X by the value of Y.
jnz X Y jumps with an offset of the value of Y, but only if the value of X is not zero. 
(An offset of 2 skips the next instruction, an offset of -1 jumps to the previous instruction, and so on.)
Only the instructions listed above are used. The eight registers here, named a through h, all start at 0.

The coprocessor is currently set to some kind of debug mode, which allows for testing, 
but prevents it from doing any meaningful work.

If you run the program (your puzzle input), how many times is the mul instruction invoked?

--- Part Two ---
Now, it's time to fix the problem.

The debug mode switch is wired directly to register a. You flip the switch, which makes register a now start at 1 
when the program is executed.

Immediately, the coprocessor begins to overheat. Whoever wrote this program obviously didn't choose a very efficient implementation. 
You'll need to optimize the program if it has any hope of completing before Santa needs that printer working.

The coprocessor's ultimate goal is to determine the final value left in register h once the program completes. 
Technically, if it had that... it wouldn't even need to run the program.

After setting register a to 1, if the program were to run to completion, what value would be left in register h?
 */
object Day23CoprocessorConflagration {

    fun solve(input: String): Long {
        val instructions: List<Instruction> = input.split("\n").map {
            val tokens = it.split(" ")
            when (tokens[0]) {
                "set" -> when (tokens[2]) {
                    in intRegex -> Set(tokens[1], tokens[2].toLong())
                    in regRegex -> SetFromRegister(tokens[1], tokens[2])
                    else -> throw IllegalArgumentException("set")
                }
                "sub" -> when (tokens[2]) {
                    in intRegex -> Sub(tokens[1], tokens[2].toInt())
                    in regRegex -> SubRegister(tokens[1], tokens[2])
                    else -> throw IllegalArgumentException("add")
                }
                "mul" -> when (tokens[2]) {
                    in intRegex -> Mul(tokens[1], tokens[2].toInt())
                    in regRegex -> MulRegister(tokens[1], tokens[2])
                    else -> throw IllegalArgumentException("mul")
                }
                "jnz" -> {
                    if (tokens[1] in intRegex && tokens[2] in intRegex)
                        Jnz(tokens[1].toLong(), tokens[2].toLong())
                    else if (tokens[1] in regRegex && tokens[2] in intRegex)
                        Jrnz(tokens[1], tokens[2].toLong())
                    else Jrnzr(tokens[1], tokens[2])
                }
                else -> throw IllegalArgumentException("instr")
            }
        }

        val registers = instructions.fold(linkedMapOf(Pair("jmp", 0L)), { acc: Map<String, Long>, instr ->
            acc.plus(Pair(instr.destReg, 0L))
        }).plus(Pair("a", 1L))

        return run(registers, instructions, 0L, 0L)["h"]!!
    }

    private tailrec fun run(registers: Map<String, Long>, instructions: List<Instruction>, idx: Long, mulCounter: Long): Map<String, Long> {
        if (idx >= instructions.size) return registers

        if (registers["jmp"] != 0L)
            return run(registers.plus(Pair("jmp", 0L)), instructions, idx - 1 + registers["jmp"]!!, mulCounter)

        val newMulCounter = if (instructions[idx.toInt()] is Mul || instructions[idx.toInt()] is MulRegister)
            mulCounter + 1 else mulCounter

        return run(instructions[idx.toInt()].eval(registers), instructions, idx + 1, newMulCounter)
    }

}

private val inputFileName = "day23.txt"

fun main(args: Array<String>) {
    val input = FileUtil.readFile(inputFileName)

    println(Day23CoprocessorConflagration.solve(input))
}
