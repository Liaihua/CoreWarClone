package com.example.corewarclone.memoryArrayActivity.vm

import com.example.corewarclone.ProgramFileManager
import com.example.corewarclone.memoryArrayActivity.translator.INSTRUCTION_BYTES_COUNT
import java.util.*
import kotlin.random.Random

const val MEMORY_ARRAY_SIZE = 5120

var MemoryArray = arrayOf<Instruction>()

// Метод для вычисления индекса массива в случаях, когда индекс больше размера массива
// (реализация массива-кольца)
fun calculateRound(arraySize: Int, index: Int) : Int {
    var currentIndex = index
    while(kotlin.math.abs(currentIndex) >= arraySize)
    {
        if(currentIndex >= 0)
            currentIndex -= arraySize
        else
            currentIndex += arraySize
    }
    return if(currentIndex >= 0)
        currentIndex
    else
        arraySize + currentIndex
}

class Loader {
    private val programFileManager = ProgramFileManager
    var loadedWarriors = arrayOf<Warrior>()
    var loadedWarriorsBounds = arrayOf<Pair<Int, Int>>()

    private fun checkMagicNum(magicNumByteArray: ByteArray) : Boolean {
        val magicNum = (magicNumByteArray[0].toInt() shl 8) or magicNumByteArray[1].toInt()
        if(magicNum != 0xbeda)
            return false
        return true
    }

    // Kotlin все еще не умеет в работу с шортами
    private fun getShort(bytes: ByteArray) : Int {
        return ((bytes[0].toInt() and 0xFF) shl 8) or (bytes[1].toInt() and 0xFF)
    }

    private fun getInt(bytes: ByteArray) : Int {
        val value = // Откуда на конце берется бит?
            // Видимо, во время преобразования типов из байта (со знаком!) в целый,
            // отрицательные значения так же преобразуются в отрицательные
            ((bytes[0].toInt() and 0xFF) shl 24) or
            ((bytes[1].toInt() and 0xFF) shl 16) or
            ((bytes[2].toInt() and 0xFF) shl 8) or
            ((bytes[3].toInt() and 0xFF))
        return value
    }

    fun loadProgramFile(binaryFile: ByteArray) : Array<Instruction>? {

        // Проверка файла на правильный формат
        if(checkMagicNum(binaryFile.copyOfRange(0, 2)))
            return null

        // Получение количества инструкций
        val instructionsAmount = getShort(binaryFile.copyOfRange(2, 4))

        var loadedProgram = arrayOf<Instruction>()

        var instructionIndex = 0

        while(instructionIndex < instructionsAmount)
        {
            val instruction = Instruction(
                opcode = binaryFile[4 + instructionIndex],
                operandA = getInt(binaryFile.copyOfRange(4 + instructionIndex + 1, 4 + instructionIndex + 5)),
                operandB = getInt(binaryFile.copyOfRange(4 + instructionIndex + 5, 4 + instructionIndex + 9))
            )
            loadedProgram += instruction
            instructionIndex += INSTRUCTION_BYTES_COUNT
        }

        return loadedProgram
    }

    fun initializeMemoryArray(filePaths: List<String>) : Array<Instruction> {
        var memoryArray = Array<Instruction>(MEMORY_ARRAY_SIZE) { Instruction(opcode = 0, operandA = 0, operandB = 0) }
        occupiedIndices = Array(MEMORY_ARRAY_SIZE) { -1 }
        var warriorCount = 0
        val rand = Random
        for(filePath in filePaths) {

            // 1. Читаем бинарный код программы
            // 2. Выбираем случайную точку в массиве (сверяясь с записями границ других программ) и сохраняем точки записи программы в массиве
            // 3. Записываем бин
            // 4. Создаем Warrior'а и присваиваем ему Task с InstructionPointer на выбранную точку

            val programBinary = programFileManager.readBinaryFile(filePath)
            val loadedProgram = loadProgramFile(programBinary) ?: return arrayOf()

            var startIndex = rand.nextInt(MEMORY_ARRAY_SIZE)
            var currentIndex = startIndex

            while(true) {
                if((loadedWarriorsBounds.filter { startEnd ->
                        (startIndex >= startEnd.first && startIndex <= startEnd.second) ||
                        (loadedProgram.count() - 1 <= startEnd.second && loadedProgram.count() - 1 >= startEnd.first)
                    }).isNotEmpty())
                        startIndex = rand.nextInt(MEMORY_ARRAY_SIZE)
                else {
                    loadedWarriorsBounds += Pair(startIndex, startIndex + loadedProgram.count())
                    break
                }
            }

            for(instruction in loadedProgram) {
                memoryArray[calculateRound(MEMORY_ARRAY_SIZE, currentIndex)] = instruction
                currentIndex++
            }

            val warrior = Warrior()
            warrior.name = filePath.split(".").first()
            warrior.id = warriorCount++
            val task = Task()
            task.instructionPointer = startIndex
            warrior.taskQueue = ArrayDeque()
            warrior.taskQueue.add(task)
            loadedWarriors += warrior


            for (index in startIndex..currentIndex) {
                occupiedIndices[index] = warrior.id
            }
        }

        Warriors = ArrayDeque(loadedWarriors.toMutableList())
        (Warriors as MutableList<Warrior>).shuffle()
        return memoryArray
    }
}