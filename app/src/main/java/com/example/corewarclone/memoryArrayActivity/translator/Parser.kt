package com.example.corewarclone.memoryArrayActivity.translator

import java.util.*
import kotlin.*
import kotlin.experimental.and
import kotlin.math.absoluteValue

const val INSTRUCTION_BYTES_COUNT = 9 // Количество бит на одну инструкцию

data class Error(var line: Int, val description: String)

class Parser {

    // Каким образом я буду отправлять и исполнять инструкции:
    // Опкод - фактически (учитывая версию реализуемого стандарта), его можно выразить в виде одного байта
    // Операнды - первые два байта можно использовать в качестве собственно числа. Остальные два - в качестве режима адресации
    // Итого: инструкция будет принимать следующий вид:
    // op   Am    A   Bm    B
    // 00 0000 0000 0000 0000

    // Метки
    // Метки будут заменяться перед

    val labelSeparator   = ":"
    val operandSeparator = ","
    val commentSeparator = ";"
    val opcodes = arrayOf(
        "dat", // Инструкция для хранения данных, при исполнении завершает текущую задачу
        "mov", // Инструкция для копирования данных (в случае Immediate Address Mode) или инструкции (в остальных случаях)
        "add", // Складывает значение из операнда A со значением из B, сохраняя в B
        "sub", // Вычитает B из A и сохраняет значение в B
        "jmp", // Меняет IP на значение из операнда A
        "jmz", // Меняет IP на значение из операнда A, если B == 0
        "jmn", // Меняет IP на значение из операнда A, если B != 0
        "djn", // Декрементирует значение из B по ссылке из операнда B. Если значение B != 0, меняем IP на значение из A
        "cmp", // Сравнивает данные (в случае использования явной адресации) или инструкции (в случае использования другого типа адресации)
               // и пропускает следующую инструкцию, если значения равны
        "spl"  // Создает новую задачу,
    )

    val addressingModes = arrayOf(
        '#', // Immediate Address Mode (явное значение)
        '$', // Direct Address Mode (прямая адресация, инструкция относительно текущей инструкции)
        '@', // Indirect Address Mode (непрямая адресация, пока не знаю, каким образом она работает)
        '<'  // Decrement Indirect Address Mode (то же, что и непрямая адресация, но с инкрементом)
    )

    var parsedLabels = HashMap<String, Int>()

    var parsedInstructions = ByteArray(0)

    private fun getCharAt(string: String, index: Int) : Char = string[index]

    fun convertInt16ToByteArray(value: Short) : ByteArray {
        val array = ByteArray(2)
        array[1] = (value.toInt() and 0xFFFF).toByte()
        array[0] = ((value.toInt() shr 8) and 0xFFFF).toByte()
        return array
    }

    fun convertInt32ToByteArray(value: Int) : ByteArray {
        val array = ByteArray(4)
        array[3] = (value and 0xFFFF).toByte()
        array[2] = ((value ushr 8) and 0xFFFF).toByte()
        array[1] = ((value ushr 16) and 0xFFFF).toByte()
        array[0] = ((value ushr 24) and 0xFFFF).toByte()
        return array
    }
    // Передает строку без комментария
    fun preprocessComments(instructionText: String) : String {
        return instructionText.split(commentSeparator)[0]
    }

    // Просматривает файл на наличие меток
    fun preprocessLabels(fileText: String) : Error? {
        val instructions = fileText.split("\n").filter { it.isNotBlank() }
        var instructionCount = 0
        while(instructionCount <= instructions.lastIndex)
        {
            val instruction = instructions[instructionCount].trim()
            val separated = instruction.split(labelSeparator)
            if(separated.count() == 2)
            {
                val label = separated[0].trim()
                if(label.isBlank())
                    return Error(line = -1, description = "ERROR_NO_LABEL")
                parsedLabels[label] = instructionCount
            }
            instructionCount++
        }
        return null
    }

    fun parseInstruction(line: String, instructionCount: Int) : Error? {
        if(line.isBlank())
            return null

        val instruction = line.trim().split(" ", limit = 2).filter { it.isNotBlank() }

        val opcodeResult = parseOpcode(instruction[0]) // Опкод
        if(opcodeResult != null)
            return opcodeResult

        val operandResult = parseOperands(instruction[1], instructionCount) // Операнды
        if(operandResult != null)
            return operandResult

        return null
    }

    fun parseOperands(operands: String, instructionCount: Int) : Error? {
        val parsedOperands = operands.replace(" ", "").split(operandSeparator)

        if(parsedOperands.count() != 2) // Разделитель отсутствует
            return Error (line = -1, description = "ERROR_NO_SEPARATOR")

        val operandA = parsedOperands[0]
        var operandAMode = 1 // Режим прямой адресации, в случае если у операнда не указан другой режим
        var operandAValue = operandA.toIntOrNull()

        if(operandAValue == null) {
            if(addressingModes.none{ it == operandA[0] }){
                if(parsedLabels.keys.contains<String>(operandA)) {
                    operandAValue = parsedLabels[operandA]!! + 1 - instructionCount
                }
                else {
                    return Error(line = -1, description = "ERROR_WRONG_ADDRESSING_MODE_OPERAND_A")
                }
            }
            else {
                operandAMode = addressingModes.indexOf(operandA[0])
                operandAValue = operandA.drop(1).toIntOrNull() // Убираем первый символ
                if(operandAValue == null) {
                    var label = operandA
                    if(operandAMode != -1) { // Если указан режим адресации
                        label = label.drop(1)
                    }
                    if(parsedLabels.keys.contains<String>(label)) // Проверка на наличие метки
                    {
                        operandAValue = parsedLabels[label]!! + 1 - instructionCount
                    }
                    else {
                        return Error(line = -1, description = "ERROR_WRONG_VALUE_OPERAND_A")
                    }
                }
                if(operandAValue!!.absoluteValue > 0xFFFF) { // Проверка на переполнение
                    return Error (line = -1, description = "ERROR_OVERFLOW_OPERAND_A")
                }
            }
        }

        operandAValue = operandAValue.and(0x0000FFFF).or(operandAMode shl 16)
        parsedInstructions += convertInt32ToByteArray(operandAValue)

        val operandB = parsedOperands[1]
        var operandBMode = 1
        var operandBValue = operandB.toIntOrNull()

        if(operandBValue == null) {
            if(addressingModes.none { it == operandB[0] }) {
                if(parsedLabels.keys.contains<String>(operandB)) {
                    operandBValue = parsedLabels[operandB]!! + 1 - instructionCount
                }
                else {
                    return Error(line = -1, description = "ERROR_WRONG_ADDRESSING_MODE_OPERAND_B")
                }
            }
            else {
                operandBMode = addressingModes.indexOf(operandB[0])
                operandBValue = operandB.drop(1).toIntOrNull()
                if(operandBValue == null) {
                    var label = operandB
                    if(operandBMode != -1) {
                        label = label.drop(1)
                    }
                    if(parsedLabels.keys.contains<String>(label))
                    {
                        operandBValue = parsedLabels[label]!! + 1 - instructionCount
                    }
                    else {
                        return Error(line = -1, description = "ERROR_WRONG_VALUE_OPERAND_B")
                    }
                }
                if(operandBValue.absoluteValue > 0xFFFF) {
                    return Error(line = -1, description = "ERROR_OVERFLOW_OPERAND_B")
                }
            }
        }
        operandBValue = operandBValue.and(0x0000FFFF).or(operandBMode shl 16)
        parsedInstructions += convertInt32ToByteArray(operandBValue)

        return null
    }

    fun parseOpcode(opcode: String) : Error? {
        val loweredOpcode = opcode.toLowerCase(Locale.ROOT)
        val opcodeResult = opcodes.indexOf(loweredOpcode)
        if(opcodeResult == -1)
            return Error (line = -1, description = "ERROR_UNKNOWN_OPCODE")
        parsedInstructions += opcodeResult.toByte()
        return null
    }

    fun parseAll(fileText: String) : Error? {

        preprocessLabels(fileText)

        val instructions = fileText.split("\n")
        var line = 0
        var instructionsCount = 0 // Подсчет инструкций. Необходимо для адресации между меток

        while(line <= instructions.lastIndex)
        {
            var instruction = instructions[line].trim()

            instruction = instruction.split(":").last() // Избавляемся от метки, так как мы уже записали ее в HashMap

            instruction = preprocessComments(instruction)

            if (instruction.isNotBlank())
                instructionsCount++
            else {
                line++
                continue
            }
            val result = parseInstruction(instruction, instructionsCount)
            if(result == null) {
                line++
            }
            else {
                result.line = line + 1
                return result
            }
        }
        return null
    }
}