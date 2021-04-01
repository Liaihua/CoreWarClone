package com.example.corewarclone.memoryArrayActivity.translator
import com.example.corewarclone.mainActivity.ProgramFileManager

const val MAGIC_NUM = 0xbeda

class Translator {
    private val programFileManager = ProgramFileManager
    private val parser: Parser = Parser()
    // Преобразует .red файл в .rbin
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
        parser.parseAll(programFileManager.readProgramFile(fileName))
            ?: return parser.parsedInstructions
        return null
    }
}