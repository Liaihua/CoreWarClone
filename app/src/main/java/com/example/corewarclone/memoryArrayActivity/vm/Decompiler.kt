package com.example.corewarclone.memoryArrayActivity.vm

// Объект для преобразования набора байт в понятный набор символов
// Используется в warriors_recycler_view
object Decompiler {
    private fun decompileOpcode(opcode: Byte) : String {
        return when (opcode) {
            0.toByte() -> "DAT"
            1.toByte() -> "MOV"
            2.toByte() -> "ADD"
            3.toByte() -> "SUB"
            4.toByte() -> "JMP"
            5.toByte() -> "JMZ"
            6.toByte() -> "JMN"
            7.toByte() -> "DJN"
            8.toByte() -> "CMP"
            9.toByte() -> "SPL"
            else -> ""
        }
    }
    private fun decompileOperand(operand: Int) : String {
        val addressMode = (operand shr 16) and 0xFF
        val operandValue = (operand and 0xFFFF).toShort()
        val addressModeString = when (addressMode) {
            0 -> "#"
            1 -> "$"
            2 -> "@"
            3 -> "<"
            else -> ""
        }
        return "${addressModeString}${operandValue}"
    }
    fun decompileInstruction(instruction: Instruction) : String {
        val resultOpcode = decompileOpcode(instruction.opcode)
        val resultOperandA = decompileOperand(instruction.operandA)
        val resultOperandB = decompileOperand(instruction.operandB)
        return "$resultOpcode $resultOperandA, $resultOperandB"
    }
}