package com.example.corewarclone.memoryArrayActivity.vm

import java.util.ArrayDeque

class Executor {

    // # - 0,
    // $ - 1,
    // @ - 2,
    // < - 3

    // TODO Сделать проверку на наличие багов во время всяких присваиваний
    // (А если более конкретно, то проверку на перезапись режимов адресации
    // (хотя кто знает, что еще мне попадется))
    // А может, стоит попробовать отладку на массиве меньших размеров?

    private fun getOperandValue(operand: Int) : Short {
        return (operand and 0xFFFF).toShort()
    }

    private fun getOperandAddressMode(operand: Int) : Int {
        return (operand shr 16) and 0xFF
    }

    // Метод не устанавливает значение указанного операнда, но возвращает значение,
    // которое можно использовать для последующего присваивания
    private fun setOperandValue(operand: Int, value: Short) : Int {
        return (operand and 0xFFFF0000.toInt()) or (value.toInt() and 0x0000FFFF)
    }

    // Вопрос: а разве адресация может меняться во время исполнения?
    private fun setOperandAddressMode(operand: Int, value: Short) : Int {
        return (operand and 0xFFFF) or (value.toInt() shl 16)
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
                val indirectInstruction = MemoryArray[calculateRound(MEMORY_ARRAY_SIZE, getOperandValue(instruction.operandA) + position)]
                operandAAddress = (getOperandValue(indirectInstruction.operandA) - 1.toShort()).toShort()
            }
            // <
            3 -> {
                val indirectInstruction = MemoryArray[calculateRound(MEMORY_ARRAY_SIZE, getOperandValue(instruction.operandA) + position)]
                indirectInstruction.operandA -= setOperandValue(indirectInstruction.operandA, 1)
                operandAAddress = getOperandValue(indirectInstruction.operandA)
            }
            else -> return null

        }

        when(getOperandAddressMode(instruction.operandB)) {

            // #
            0 -> {

            }
            // $
            1 -> {
                operandBAddress = getOperandValue(instruction.operandB)
            }
            // @
            2 -> {
                val indirectInstruction = MemoryArray[calculateRound(MEMORY_ARRAY_SIZE, getOperandValue(instruction.operandB) + position)]
                operandBAddress = (getOperandValue(indirectInstruction.operandB) - 1.toShort()).toShort()
            }
            // <
            3 -> {
                val indirectInstruction = MemoryArray[calculateRound(MEMORY_ARRAY_SIZE, getOperandValue(instruction.operandB) + position)]
                indirectInstruction.operandB -= setOperandValue(indirectInstruction.operandB, 1)
                operandBAddress = getOperandValue(indirectInstruction.operandB)
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
                    // Если A установлен в '#',
                    if(operandsModes.first == 0) {
                        MemoryArray[calculateRound(
                            MEMORY_ARRAY_SIZE,
                            task.instructionPointer + operandsAddresses.second
                        )]
                            .operandA = instruction.operandA
                    }
                    // Если A установлен в другие режимы
                    else {
                        MemoryArray[calculateRound(
                            MEMORY_ARRAY_SIZE,
                            task.instructionPointer + operandsAddresses.second
                        )] = instruction
                    }
                    return 1
                }
            }

            // ADD
            2.toByte() -> {
                // Так как B является ссылкой на операнд назначения, нам нужен любой режим адресации, кроме явного
                return if(operandsModes.second != 0) {
                    val processedInstruction = MemoryArray[calculateRound(MEMORY_ARRAY_SIZE, task.instructionPointer + operandsAddresses.second)]
                    processedInstruction.operandB += getOperandValue(MemoryArray[calculateRound(MEMORY_ARRAY_SIZE, task.instructionPointer)].operandA)
                    1
                }
                else {
                    null
                }
            }

            // SUB
            3.toByte() -> {
                // Читай комментарий к ADD
                return if(operandsModes.second != 0) {
                    val processedInstruction = MemoryArray[calculateRound(MEMORY_ARRAY_SIZE, task.instructionPointer + operandsAddresses.second)]
                    processedInstruction.operandB -= getOperandValue(MemoryArray[calculateRound(MEMORY_ARRAY_SIZE, task.instructionPointer)].operandA)
                    1
                }
                else {
                    null
                }
            }

            // JMP
            4.toByte() -> {
                // Здесь мы просто возвращаем адрес текущей инструкции + адрес, взятый из операнда A
                return operandsAddresses.first.toInt()
            }

            // JMZ
            5.toByte() -> {
                // Повторяем предыдущий пункт, предварительно делая проверку B == 0?
                return if(getOperandValue(
                        MemoryArray[calculateRound(MEMORY_ARRAY_SIZE, task.instructionPointer + operandsAddresses.second)]
                            .operandB) == 0.toShort()) {
                    operandsAddresses.first.toInt()
                } else {
                    1
                }
            }

            // JMN
            6.toByte() -> {
                // Теперь наоборот, B != 0
                return if(getOperandValue(
                        MemoryArray[calculateRound(MEMORY_ARRAY_SIZE, task.instructionPointer + operandsAddresses.second)]
                            .operandB) != 0.toShort()) {
                    operandsAddresses.first.toInt()
                }
                else {
                    1
                }
            }

            // DJN
            7.toByte() -> {
                // 1. Мы уменьшаем значение из B
                // 2. Затем делаем проверку на B != 0
                instruction.operandB = setOperandValue(instruction.operandB, (getOperandValue(instruction.operandB) - 1).toShort())
                if(instruction.operandB != 0) {
                    operandsAddresses.first.toInt()
                }
                else {
                    1
                }
            }

            // CMP
            8.toByte() -> {
                // Мы сравниваем значения из A и B, и если они совпадают, мы пропускаем следующую инструкцию
                // Оба значения заданы явно
                return if(operandsModes == Pair(0, 0)) {
                    if(getOperandValue(instruction.operandA) == getOperandValue(instruction.operandB))
                        2
                    else
                        1

                }
                else
                {
                    // Одно из значений (или оба) задано/заданы в виде относительного адреса
                    if(operandsModes.first == 0) {
                        if(getOperandValue(instruction.operandA) == getOperandValue(MemoryArray[calculateRound(
                                MEMORY_ARRAY_SIZE, task.instructionPointer + operandsAddresses.second)].operandB)) {
                            2
                        }
                        else {
                            1
                        }
                    } else if(operandsModes.second == 0) {
                        if (getOperandValue(instruction.operandB) == getOperandValue(MemoryArray[calculateRound(
                                MEMORY_ARRAY_SIZE, task.instructionPointer + operandsAddresses.first)].operandA)) {
                            2
                        }
                        else {
                            1
                        }
                    }
                    else {
                        if(getOperandValue(MemoryArray[calculateRound(MEMORY_ARRAY_SIZE, task.instructionPointer + operandsAddresses.first)].operandA)
                            == getOperandValue(MemoryArray[calculateRound(MEMORY_ARRAY_SIZE, task.instructionPointer + operandsAddresses.second)].operandB)) {
                            2
                        }
                        else {
                            1
                        }
                    }
                }
            }

            // SPL
            9.toByte() -> {
                if(warrior.taskQueue.count() <= MAX_TASKS)
                {
                    val newTask = Task()
                    newTask.instructionPointer = operandsAddresses.first.toInt()
                    warrior.taskQueue.add(newTask)
                }
                return 1
            }
        }
        return null
    }
}