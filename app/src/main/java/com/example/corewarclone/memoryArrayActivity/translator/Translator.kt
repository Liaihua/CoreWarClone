package com.example.corewarclone.memoryArrayActivity.translator
import com.example.corewarclone.mainActivity.ProgramFileManager
import java.io.*

class Translator {
    val programFileManager = ProgramFileManager
    val parser: Parser = Parser()
    fun translate(fileName: String) {
        parser.parseAll(programFileManager.readProgramFile(fileName))
        if(parser.finishedSuccessfully)
        {

        }
    }
}