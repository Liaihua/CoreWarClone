package com.example.corewarclone.mainActivity

import java.io.*
import java.util.*
import kotlin.*

const val dir_name = "redcode";

// ProgramFile используется для представления основных файлов, хранимых в папке приложения
data class ProgramFile (val name: String, val last_edit: Long, val size: Long);

class ProgramFileManager {

    // Метод создает директорию "redcode/" (мспользуется в случае, если директория отсутствует)
    private fun initializeRedcodeDirectory() {
        File(dir_name).mkdir()
    }

    // Метод используется для отображения файлов, которые находятся в поддиректории приложения "redcode/"
    fun listProgramFiles() : Array<ProgramFile>? {

        val redcodeDir = File(dir_name)

        if(!redcodeDir.exists()) {
            initializeRedcodeDirectory()
            return null
        }

        var programFiles = arrayOf<ProgramFile>()

        for (redcodeFile in redcodeDir.listFiles())
        {
            val pf = ProgramFile(
                redcodeFile.name,
                redcodeFile.lastModified(),
                redcodeFile.totalSpace
            )

            programFiles += pf
        }

        return programFiles
    }

    // Метод используется для сохранения программы из редактора в папку "redcode/"
    fun saveProgramFile() {

    }
}