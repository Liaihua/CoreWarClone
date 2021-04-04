package com.example.corewarclone.memoryArrayActivity.vm

import com.example.corewarclone.ProgramFileManager
import com.example.corewarclone.memoryArrayActivity.translator.INSTRUCTION_BYTES_COUNT
import java.io.File

const val MEMORY_ARRAY_SIZE = 8000

class Loader {
    private val programFileManager = ProgramFileManager

    private fun checkMagicNum(magicNumByteArray: ByteArray) : Boolean {
        val magicNum = (magicNumByteArray[0].toInt() shl 8) or magicNumByteArray[1].toInt()
        if(magicNum != 0xbeda)
            return false
        return true
    }

    // Kotlin все еще не умеет в работу с шортами
    private fun getShort(bytes: ByteArray) : Int {
        return (bytes[0].toInt() shl 8) or bytes[1].toInt()
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
        for(filePath in filePaths) {
            val programBinary = programFileManager.readBinaryFile(filePath)
            loadProgramFile(programBinary)
        }
        return arrayOf()
    }
}