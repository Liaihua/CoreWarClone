package com.example.corewarclone.memoryArrayActivity.vm

class Executor {
    // Выполнение инструкции. Метод возвращает число, на которое увеличить Instruction Pointer
    // Если возвращается null, то выполняемый Task завершается

    // #, $, @, <

    fun execute(instruction: Instruction) : Int? {
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