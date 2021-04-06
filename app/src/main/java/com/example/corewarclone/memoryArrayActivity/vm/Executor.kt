package com.example.corewarclone.memoryArrayActivity.vm

import java.util.ArrayDeque

class Executor {

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
    private fun calculateAddress(instruction: Instruction, position: Int) : Pair<Short, Short>? {

        var operandAAddress = 0.toShort()
        var operandBAddress = 0.toShort()
        when(getOperandAddressMode(instruction.operandA)) {

            // #
            0 -> {

            }
            // $
            1 -> {
                operandAAddress = getOperandValue(instruction.operandA)
            }
            // @
            2 -> {
                operandAAddress = getOperandValue(
                    MemoryArray[calculateRound(MEMORY_ARRAY_SIZE, position)].operandA)
            }
            // <
            3 -> {

            }
            else -> return null

        }

        when(getOperandAddressMode(instruction.operandB)) {

            // #
            0 -> {}
            // $
            1 -> {
                operandBAddress = getOperandValue(instruction.operandB)
            }
            // @
            2 -> {
                operandBAddress = getOperandValue(
                    MemoryArray[calculateRound(MEMORY_ARRAY_SIZE, position)].operandB)
            }
            // <
            3 -> {

            }
            else -> return null
        }

        return Pair(operandAAddress, operandBAddress)
    }

    // Метод execute выполняет текущую инструкцию и ВОЗВРАЩАЕТ ЧИСЛО (А НЕ ВРУЧНУЮ МАНИПУЛИРУЕТ ВСЕМ И СРАЗУ.
    // Доверь это дело другим классам и методам),
    // на которое увеличить Instruction Pointer
    // Если возвращается null, то выполняемый Task завершается
    fun execute(warrior: Warrior, task: Task, instruction: Instruction) : Int? {
        // Мы предварительно вычисляем адреса, на которые ссылаются операнды, дабы ускорить нашу работу
        val operandsAddresses = calculateAddress(instruction, task.instructionPointer) ?: return null

        // Мы дополнительно берем режимы адресации, дабы не спутать $0 с #0
        // (ведь calculateAddress вычисляет именно значение адреса, но не уточняет, какой режим
        // адресации установлен для данного операнда)
        val operandsModes = Pair(
            getOperandAddressMode(instruction.operandA),
            getOperandAddressMode(instruction.operandB))

        when(instruction.opcode) {
            // DAT
            0.toByte() -> {
                // Независимо от содержимого, при выполнении данной инструкции мы завершаем текущую задачу
                return null
            }

            // MOV
            1.toByte() -> {
                // Проверка операнда B на правильный режим адресации. Если неправильный - задача завершается
                if(operandsModes.second == 0) {
                    return null
                }
                else {

                }
            }

            // ADD
            2.toByte() -> {
                // Так как B является ссылкой на операнд назначения, нам нужен любой режим адресации, кроме явного
                if(operandsModes.second != 0) {

                }
                else {
                    return null
                }
            }

            // SUB
            3.toByte() -> {
                // Читай комментарий к ADD
                if(operandsModes.second != 0) {

                }
                else {
                    return null
                }
            }

            // JMP
            4.toByte() -> {
                // Здесь мы просто возвращаем адрес текущей инструкции + адрес, взятый из операнда A
                return calculateRound(MEMORY_ARRAY_SIZE, task.instructionPointer + operandsAddresses.first)
            }

            // JMZ
            5.toByte() -> {
                // Повторяем предыдущий пункт, предварительно делая проверку B == 0?
                return if(getOperandValue(
                        MemoryArray[calculateRound(MEMORY_ARRAY_SIZE, task.instructionPointer + operandsAddresses.second)]
                            .operandB) == 0.toShort()) {
                    calculateRound(MEMORY_ARRAY_SIZE, task.instructionPointer + operandsAddresses.first)
                } else {
                    calculateRound(MEMORY_ARRAY_SIZE, ++task.instructionPointer)
                }
            }

            // JMN
            6.toByte() -> {
                // Теперь наоборот, B != 0
                return if(getOperandValue(
                        MemoryArray[calculateRound(MEMORY_ARRAY_SIZE, task.instructionPointer + operandsAddresses.second)]
                            .operandB) != 0.toShort()) {
                    calculateRound(MEMORY_ARRAY_SIZE, task.instructionPointer + operandsAddresses.first)
                }
                else {
                    calculateRound(MEMORY_ARRAY_SIZE, ++task.instructionPointer)
                }
            }

            // DJN
            7.toByte() -> {

            }

            // CMP
            8.toByte() -> {
                // Мы сравниваем значения из A и B, и если они совпадают, мы пропускаем следующую инструкцию
                
            }

            // SPL
            9.toByte() -> {
                if(warrior.taskQueue.count() <= MAX_TASKS)
                {
                    val newTask = Task()
                    newTask.instructionPointer = operandsAddresses.first.toInt()
                    warrior.taskQueue.add(newTask)
                }
                return ++task.instructionPointer
            }
        }
        return null
    }
}