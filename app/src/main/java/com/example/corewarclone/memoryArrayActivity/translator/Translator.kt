package com.example.corewarclone.memoryArrayActivity.translator
import com.example.corewarclone.mainActivity.ProgramFileManager
import java.io.File

const val MAGIC_NUM = 0xbeda

class Translator {
    private val programFileManager = ProgramFileManager
    private val parser: Parser = Parser()
    // Преобразует .red файл в .rbin
    fun translate(fileName: String) : Error? {
        val preprocessedFileName: String
        val result: Error?

        if(fileName.split(File.separator).size == 1) {
            preprocessedFileName = fileName
            result = parser.parseAll(programFileManager.readProgramFile(fileName))
        } else {
            preprocessedFileName = fileName.split(File.separator).last()
            result = parser.parseAll(programFileManager.readProgramFileByAbsolutePath(fileName))
        }

        if(result == null)
        {
            // Разделяет имя файла, берет его название и присваивает новое расширение
            val binaryFileName = preprocessedFileName.split(".")[0].plus(".rbin")
            var binaryFile = ByteArray(0)
            binaryFile += parser.convertInt16ToByteArray(MAGIC_NUM.toShort())
            binaryFile += parser.convertInt16ToByteArray(parser.parsedInstructions.size.toShort())
            binaryFile += parser.parsedInstructions
            programFileManager.writeBinaryFile(binaryFileName, binaryFile)
            return null
        }
        return result
    }

    fun showBytes(sourceCode: String) : ByteArray? {
        val parsed = parser.parseAll(sourceCode)
        if(parsed != null)
            return null
        return parser.parsedInstructions

    }
}