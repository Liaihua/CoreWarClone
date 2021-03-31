package com.example.corewarclone.memoryArrayActivity.translator
import com.example.corewarclone.mainActivity.ProgramFileManager
import java.io.*

class Translator {
    val programFileManager = ProgramFileManager
    val parser: Parser = Parser()
    fun translate(fileName: String) {
        val result = parser.parseAll(programFileManager.readProgramFile(fileName))
        if(result == null)
        {
            // TODO найти место записи rbin-файла
            val fileToWrite = File("something")
            fileToWrite.writeBytes(parser.parsedInstructions)
        }
    }
}