package com.example.corewarclone.memoryArrayActivity.vm

class Executor {
    // Метод execute выполняет текущую инструкцию и ВОЗВРАЩАЕТ ЧИСЛО (А НЕ ВРУЧНУЮ МАНИПУЛИРУЕТ ВСЕМ И СРАЗУ),
    // на которое увеличить Instruction Pointer
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

    // Вычисляет значение относительного адреса
    private fun calculateAddress(instruction: Instruction) : Pair<Int, Int>? {

        var operandAAddress = 0
        var operandBAddress = 0
        when(getOperandAddressMode(instruction.operandA)) {

            // #
            0 -> {

            }
            // $
            1 -> {}
            // @
            2 -> {}
            // <
            3 -> {}
            else -> return null

        }

        when(getOperandAddressMode(instruction.operandB)) {

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

        return Pair(operandAAddress, operandBAddress)
    }

    fun execute(warrior: Warrior, task: Task, instruction: Instruction) : Int? {
        // Мы предварительно вычисляем адреса, на которые ссылаются операнды, дабы ускорить
        val operandsAddresses = calculateAddress(instruction) ?: return null


        // Мы дополнительно берем режимы адресации, дабы не спутать $0 с #0
        // (ведь calculateAddress вычисляет именно значение адреса, но не уточняет, какой режим
        // адресации для данного операнда)
        val operandsModes = Pair(
            getOperandAddressMode(instruction.operandA),
            getOperandAddressMode(instruction.operandB))

        when(instruction.opcode) {
            // DAT
            0.toByte() -> {
                return null
            }

            // MOV
            1.toByte() -> {
                if(operandsModes.second == 0) {
                    return null
                }
                else {
                    
                }
            }

            // ADD
            2.toByte() -> {
                if(operandsModes.second != 0) {

                }
                else {
                    return null
                }
            }

            // SUB
            3.toByte() -> {
                if(operandsModes.second != 0) {

                }
                else {
                    return null
                }
            }

            // JMP
            4.toByte() -> {
                return calculateRound(MEMORY_ARRAY_SIZE, task.instructionPointer + operandsAddresses.first)
            }

            // JMZ
            5.toByte() -> {
                return if(operandsAddresses.second == 0) {
                    calculateRound(MEMORY_ARRAY_SIZE, task.instructionPointer + operandsAddresses.first)
                } else {
                    ++task.instructionPointer
                }
            }

            // JMN
            6.toByte() -> {
                if(operandsAddresses.second != 0) {
                    task.instructionPointer = calculateRound(MEMORY_ARRAY_SIZE, task.instructionPointer + operandsAddresses.first)
                }
                else {
                    task.instructionPointer++
                }
            }

            // DJN
            7.toByte() -> {}

            // CMP
            8.toByte() -> {

            }

            // SPL
            9.toByte() -> {
                if(warrior.taskQueue.count() <= MAX_TASKS)
                {
                    val newTask = Task()
                    newTask.instructionPointer = operandsAddresses.first
                    warrior.taskQueue.add(newTask)
                }
                return ++task.instructionPointer
            }
        }
        return null
    }
}