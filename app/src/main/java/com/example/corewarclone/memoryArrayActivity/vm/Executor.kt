package com.example.corewarclone.memoryArrayActivity.vm

class Executor {
    // Выполнение инструкции. Метод execute возвращает число, на которое увеличить Instruction Pointer
    // Если возвращается null, то выполняемый Task завершается

    // # - 0,
    // $ - 1,
    // @ - 2,
    // < - 3

    private fun getOperandValue(operand: Int) : Short {
        return (operand and 0xFFFF).toShort()
    }

    private fun getOperandAddressMode(operand: Int) : Int {
        return (operand shr 16) and 0xFF
    }

    private fun calculateAddress(instruction: Instruction) : Pair<Int, Int>? {

        var operandAAddress = 0
        var operandBAddress = 0
        when(getOperandAddressMode(instruction.operandA)) {

            // #
            0 -> {}
            // $
            1 -> {}
            // @
            2 -> {}
            // <
            3 -> {}
            else -> return null

        }

        when(getOperandAddressMode(instruction.operandB)) {

        }
        return null
    }

    fun execute(instruction: Instruction) : Int? {
        val operandsAddresses = calculateAddress(instruction)
        when(instruction.opcode) {
            // DAT
            0.toByte() -> {
                return null
            }

            // MOV
            1.toByte() -> {

            }

            // ADD
            2.toByte() -> {}

            // SUB
            3.toByte() -> {}

            // JMP
            4.toByte() -> {}

            // JMZ
            5.toByte() -> {}

            // JMN
            6.toByte() -> {}

            // DJN
            7.toByte() -> {}

            // CMP
            8.toByte() -> {}

            // SPL
            9.toByte() -> {}
        }
        return null
    }
}