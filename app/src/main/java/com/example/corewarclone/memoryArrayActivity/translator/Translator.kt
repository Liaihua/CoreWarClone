package com.example.corewarclone.memoryArrayActivity.translator
import com.example.corewarclone.mainActivity.ProgramFileManager

const val MAGIC_NUM = 0xbeda

class Translator {
    private val programFileManager = ProgramFileManager
    private val parser: Parser = Parser()
    // Преобразует .red файл в .rbin
    // TODO Сделать трансляцию в локальную директорию приложения
    fun translate(fileName: String) : Error? {
        val result = parser.parseAll(programFileManager.readProgramFile(fileName))
        if(result == null)
        {
            // Разделяет имя файла, берет его название и присваивает новое расширение
            val binaryFileName = fileName.split(".")[0].plus(".rbin")
            var binaryFile = ByteArray(0)
            binaryFile += parser.convertInt16ToByteArray(MAGIC_NUM.toShort())
            binaryFile += parser.convertInt16ToByteArray(parser.parsedInstructions.size.toShort())
            binaryFile += parser.parsedInstructions
            programFileManager.writeBinaryFile(binaryFileName, binaryFile)
            return null
        }
        return result
    }

    fun showBytes(fileName: String) : ByteArray? {
        val parsed = parser.parseAll(programFileManager.readProgramFile(fileName))
        if(parsed != null)
            return null
        return parser.parsedInstructions

    }
}