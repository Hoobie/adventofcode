package com.example.hoobie.adventofcode.utils

abstract class Instruction(open val destReg: String) {
    abstract fun eval(registers: Map<String, Long>): Map<String, Long>
}

data class Sound(val register: String) : Instruction(register) {
    override fun eval(registers: Map<String, Long>): Map<String, Long> {
        return registers.plus(Pair("snd", registers[register]!!))
    }
}

data class SetFromRegister(override val destReg: String, val srcReg: String) : Instruction(destReg) {
    override fun eval(registers: Map<String, Long>): Map<String, Long> {
        return registers.plus(Pair(destReg, registers[srcReg]!!))
    }
}

data class Set(val register: String, val value: Long) : Instruction(register) {
    override fun eval(registers: Map<String, Long>): Map<String, Long> {
        return registers.plus(Pair(register, value))
    }
}

data class AddRegister(override val destReg: String, val srcReg: String) : Instruction(destReg) {
    override fun eval(registers: Map<String, Long>): Map<String, Long> {
        return registers.plus(Pair(destReg, registers[destReg]!! + registers[srcReg]!!))
    }
}

data class Add(val register: String, val value: Int) : Instruction(register) {
    override fun eval(registers: Map<String, Long>): Map<String, Long> {
        return registers.plus(Pair(register, registers[register]!! + value))
    }
}

data class SubRegister(override val destReg: String, val srcReg: String) : Instruction(destReg) {
    override fun eval(registers: Map<String, Long>): Map<String, Long> {
        return registers.plus(Pair(destReg, registers[destReg]!! - registers[srcReg]!!))
    }
}

data class Sub(val register: String, val value: Int) : Instruction(register) {
    override fun eval(registers: Map<String, Long>): Map<String, Long> {
        return registers.plus(Pair(register, registers[register]!! - value))
    }
}

data class MulRegister(override val destReg: String, val srcReg: String) : Instruction(destReg) {
    override fun eval(registers: Map<String, Long>): Map<String, Long> {
        return registers.plus(Pair(destReg, registers[destReg]!! * registers[srcReg]!!))
    }
}

data class Mul(val register: String, val value: Int) : Instruction(register) {
    override fun eval(registers: Map<String, Long>): Map<String, Long> {
        return registers.plus(Pair(register, registers[register]!! * value))
    }
}

data class ModRegister(override val destReg: String, val srcReg: String) : Instruction(destReg) {
    override fun eval(registers: Map<String, Long>): Map<String, Long> {
        return registers.plus(Pair(destReg, registers[destReg]!! % registers[srcReg]!!))
    }
}

data class Mod(val register: String, val value: Int) : Instruction(register) {
    override fun eval(registers: Map<String, Long>): Map<String, Long> {
        return registers.plus(Pair(register, registers[register]!! % value))
    }
}

data class RecoverSound(val register: String) : Instruction(register) {
    override fun eval(registers: Map<String, Long>): Map<String, Long> {
        return if (registers[register]!! != 0L) registers.plus(Pair("rcv", registers["snd"]!!)) else registers
    }
}

data class Jgz(val val1: Long, val val2: Long) : Instruction("jmp") {
    override fun eval(registers: Map<String, Long>): Map<String, Long> {
        return if (val1 > 0) registers.plus(Pair("jmp", val2)) else registers
    }
}

data class Jrgz(val register: String, val value: Long) : Instruction(register) {
    override fun eval(registers: Map<String, Long>): Map<String, Long> {
        return if (registers[register]!! > 0) registers.plus(Pair("jmp", value)) else registers
    }
}

data class Jrgzr(val register: String, val srcReg: String) : Instruction(register) {
    override fun eval(registers: Map<String, Long>): Map<String, Long> {
        return if (registers[register]!! > 0) registers.plus(Pair("jmp", registers[srcReg]!!)) else registers
    }
}

data class Jnz(val val1: Long, val val2: Long) : Instruction("jmp") {
    override fun eval(registers: Map<String, Long>): Map<String, Long> {
        return if (val1 > 0) registers.plus(Pair("jmp", val2)) else registers
    }
}

data class Jrnz(val register: String, val value: Long) : Instruction(register) {
    override fun eval(registers: Map<String, Long>): Map<String, Long> {
        return if (registers[register]!! != 0L) registers.plus(Pair("jmp", value)) else registers
    }
}

data class Jrnzr(val register: String, val srcReg: String) : Instruction(register) {
    override fun eval(registers: Map<String, Long>): Map<String, Long> {
        return if (registers[register]!! != 0L) registers.plus(Pair("jmp", registers[srcReg]!!)) else registers
    }
}

data class SendRegister(val register: String) : Instruction(register) {
    override fun eval(registers: Map<String, Long>): Map<String, Long> {
        return registers.plus(Pair("snd", registers[register]!!))
    }
}

data class Send(val value: Long) : Instruction("snd") {
    override fun eval(registers: Map<String, Long>): Map<String, Long> {
        return registers.plus(Pair("snd", value))
    }
}

data class Receive(val register: String) : Instruction(register) {
    override fun eval(registers: Map<String, Long>): Map<String, Long> {
        return registers.plus(Pair("rcv", register.toCharArray()[0].toLong())) // hack
    }
}
