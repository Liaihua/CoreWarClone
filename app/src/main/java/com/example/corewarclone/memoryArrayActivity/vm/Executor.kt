package com.example.corewarclone.memoryArrayActivity.vm

import java.util.ArrayDeque

class Executor {

    // # - 0,
    // $ - 1,
    // @ - 2,
    // < - 3

    var modifiedInstruction: Int? = null

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
                val indirectInstructionIndex = calculateRound(MEMORY_ARRAY_SIZE, getOperandValue(instruction.operandA) + position)
                val indirectInstruction = MemoryArray[indirectInstructionIndex]
                operandAAddress = ((indirectInstructionIndex + getOperandValue(indirectInstruction.operandA)) - position).toShort()
            }
            // <
            3 -> {
                val indirectInstructionIndex = calculateRound(MEMORY_ARRAY_SIZE, getOperandValue(instruction.operandA) + position)
                val indirectInstruction = MemoryArray[indirectInstructionIndex]
                indirectInstruction.operandA = setOperandValue(indirectInstruction.operandA, (getOperandValue(indirectInstruction.operandA) - 1).toShort())
                operandAAddress = ((indirectInstructionIndex + getOperandValue(indirectInstruction.operandA)) - position).toShort()
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
                val indirectInstructionIndex = calculateRound(MEMORY_ARRAY_SIZE, getOperandValue(instruction.operandB) + position)
                val indirectInstruction = MemoryArray[indirectInstructionIndex]
                operandBAddress = ((indirectInstructionIndex + getOperandValue(indirectInstruction.operandB)) - position).toShort()
            }
            // <
            3 -> {
                val indirectInstructionIndex = calculateRound(MEMORY_ARRAY_SIZE, getOperandValue(instruction.operandB) + position)
                val indirectInstruction = MemoryArray[indirectInstructionIndex]
                indirectInstruction.operandB = setOperandValue(indirectInstruction.operandB, (getOperandValue(indirectInstruction.operandB) - 1).toShort())
                operandBAddress = ((indirectInstructionIndex + getOperandValue(indirectInstruction.operandB)) - position).toShort()
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

        modifiedInstruction = null

        when(instruction.opcode) {
            // DAT
            0.toByte() -> {
                // Независимо от содержимого, при выполнении данной инструкции мы завершаем текущую задачу
                return null
            }

            // MOV
            1.toByte() -> {
                // Проверка операнда B на правильный режим адресации. Если неправильный - задача завершается
                // TODO Посмотри режим адресации с декрементом
                if(operandsModes.second == 0) {
                    return null
                }
                else {
                    // Если A установлен в '#'
                    if(operandsModes.first == 0) {
                        val movedInstruction =
                        MemoryArray[calculateRound(
                            MEMORY_ARRAY_SIZE,
                            task.instructionPointer + operandsAddresses.second
                        )]
                        movedInstruction.operandB = setOperandValue(movedInstruction.operandB, instruction.operandA.toShort())
                    }
                    // Если A установлен в другие режимы
                    else {
                        val movedInstruction =
                        MemoryArray[calculateRound(
                            MEMORY_ARRAY_SIZE,
                            task.instructionPointer + operandsAddresses.first
                        )]
                        val destinationInstruction =
                            MemoryArray[calculateRound(
                                MEMORY_ARRAY_SIZE,
                                task.instructionPointer + operandsAddresses.second
                            )]
                        destinationInstruction.opcode = movedInstruction.opcode
                        destinationInstruction.operandA = movedInstruction.operandA
                        destinationInstruction.operandB = movedInstruction.operandB
                    }
                    modifiedInstruction = calculateRound(MEMORY_ARRAY_SIZE, task.instructionPointer + operandsAddresses.second)
                    return 1
                }
            }

            // ADD
            2.toByte() -> {
                // Так как B является ссылкой на операнд назначения, нам нужен любой режим адресации, кроме явного
                return if(operandsModes.second != 0) {
                    val processedInstruction = MemoryArray[calculateRound(MEMORY_ARRAY_SIZE, task.instructionPointer + operandsAddresses.second)]
                    // processedInstruction.operandB += getOperandValue(MemoryArray[calculateRound(MEMORY_ARRAY_SIZE, task.instructionPointer)].operandA)
                    processedInstruction.operandB = setOperandValue(
                        processedInstruction.operandB,
                        (MemoryArray[calculateRound(MEMORY_ARRAY_SIZE, task.instructionPointer)].operandA.toShort() +
                                getOperandValue(processedInstruction.operandB)).toShort())
                    modifiedInstruction = calculateRound(MEMORY_ARRAY_SIZE, task.instructionPointer + operandsAddresses.second)
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
                    processedInstruction.operandB = setOperandValue(
                        processedInstruction.operandB,
                        (getOperandValue(processedInstruction.operandB) -
			        MemoryArray[calculateRound(MEMORY_ARRAY_SIZE, task.instructionPointer)].operandA.toShort()).toShort())
                                modifiedInstruction = calculateRound(MEMORY_ARRAY_SIZE, task.instructionPointer + operandsAddresses.second)
                    1
                }
                else {
                    null
                }
            }

            // JMP
            4.toByte() -> {
                // Здесь мы просто возвращаем адрес текущей инструкции + адрес, взятый из операнда A
                if(operandsModes.first != 0)
                    return operandsAddresses.first.toInt()
            }

            // JMZ
            5.toByte() -> {
                // Повторяем предыдущий пункт, предварительно делая проверку B == 0?
                if(operandsModes.first != 0) {
                    return if (getOperandValue(
                            MemoryArray[calculateRound(
                                MEMORY_ARRAY_SIZE,
                                task.instructionPointer + operandsAddresses.second
                            )]
                                .operandB
                        ) == 0.toShort()
                    ) {
                        operandsAddresses.first.toInt()
                    } else {
                        1
                    }
                }
            }

            // JMN
            6.toByte() -> {
                // Теперь наоборот, B != 0
                if(operandsModes.first != 0) {
                    return if (getOperandValue(
                            MemoryArray[calculateRound(
                                MEMORY_ARRAY_SIZE,
                                task.instructionPointer + operandsAddresses.second
                            )]
                                .operandB
                        ) != 0.toShort()
                    ) {
                        operandsAddresses.first.toInt()
                    } else {
                        1
                    }
                }
            }

            // DJN
            7.toByte() -> {
                // 1. Мы уменьшаем значение по адресу из B
                // 2. Затем делаем проверку на B != 0
                if(operandsModes.first != 0) {
                    val referredInstruction = MemoryArray[calculateRound(
                        MEMORY_ARRAY_SIZE,
                        task.instructionPointer + operandsAddresses.second
                    )]
                    referredInstruction.operandB = setOperandValue(
                        referredInstruction.operandB,
                        (getOperandValue(referredInstruction.operandB) - 1).toShort()
                    )
                    return if (getOperandValue(referredInstruction.operandB) != 0.toShort()) {
                        operandsAddresses.first.toInt()
                    } else {
                        1
                    }
                }
            }

            // CMP
            8.toByte() -> {
                // Мы сравниваем значения из A и B, и если они совпадают, мы пропускаем следующую инструкцию
                // Оба значения заданы явно
                return if(operandsModes == Pair(0, 0)) {
                    if(getOperandValue(instruction.operandA) == getOperandValue(instruction.operandB))
                        1
                    else
                        2

                }
                else
                {
                    // Одно из значений (или оба) задано/заданы в виде относительного адреса
                    if(operandsModes.first == 0) {
                        if(getOperandValue(instruction.operandA) == getOperandValue(MemoryArray[calculateRound(
                                MEMORY_ARRAY_SIZE, task.instructionPointer + operandsAddresses.second)].operandB)) {
                            1
                        }
                        else {
                            2
                        }
                    } else if(operandsModes.second == 0) {
                        if (getOperandValue(instruction.operandB) == getOperandValue(MemoryArray[calculateRound(
                                MEMORY_ARRAY_SIZE, task.instructionPointer + operandsAddresses.first)].operandA)) {
                            1
                        }
                        else {
                            2
                        }
                    }
                    else {
                        if(getOperandValue(MemoryArray[calculateRound(MEMORY_ARRAY_SIZE, task.instructionPointer + operandsAddresses.first)].operandA)
                            == getOperandValue(MemoryArray[calculateRound(MEMORY_ARRAY_SIZE, task.instructionPointer + operandsAddresses.second)].operandB)) {
                            1
                        }
                        else {
                            2
                        }
                    }
                }
            }

            // SPL
            9.toByte() -> {
                if(operandsModes.first != 0) {
                    if (warrior.taskQueue.count() <= MAX_TASKS) {
                        val newTask = Task()
                        newTask.instructionPointer = calculateRound(
                            MEMORY_ARRAY_SIZE,
                            task.instructionPointer + operandsAddresses.first.toInt()
                        )
                        newTask.id = ++warrior.taskCounter
                        warrior.taskQueue.add(newTask)
                    }
                    return 1
                }
            }
        }
        return null
    }
}
