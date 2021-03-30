package com.example.corewarclone.memoryArrayActivity.translator



class Parser {

    val finishedSuccessfully = false

    // Каким образом я буду отправлять и исполнять инструкции:
    // Опкод - фактически (учитывая версию реализуемого стандарта), его можно выразить в виде одного байта
    // Операнды - первые два байта можно использовать в качестве собственно числа. Остальные два - в качестве режима адресации
    // Итого: инструкция будет принимать следующий вид:
    // op   Am    A   Bm    B
    // 00 0000 0000 0000 0000

    val labelSeparator   = ":"
    val operandSeparator = ","
    val commentSeparator = ";"
    val opcodes = arrayOf<String>(
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

    val addressing_modes = arrayOf<String>(
        "#", // Immediate Address Mode (явное значение)
        "@", // Indirect Address Mode (непрямая адресация,
        "<", // Decrement Indirect Address Mode (то же, что и непрямая адресация, но с инкрементом)
        "$"  // Direct Address Mode (прямая адресация, инструкция относительно текущей инструкции)
    )



    val parsedInstructions = arrayOf<Byte>()

    fun parseInstruction(line: String) {

    }

    fun parseOperand(operand: String) {

    }

    fun parseOpcode(opcode: String) {

    }

    fun parseAll(fileText: String) {

    }
}