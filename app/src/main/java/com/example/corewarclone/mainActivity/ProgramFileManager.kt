package com.example.corewarclone.mainActivity

// java.nio.file недоступен из-за того, что версия API, в которой он есть - 26 (Oreo)
// Повезло повезло

// https://developer.android.com/training/data-storage
// Так как я собираюсь сохранять текстовые файлы независимо от состояния приложения (допустим,
// после удаления), то мне следует уделить внимание параграфу "Documents and other files".
// Так, надеюсь, мне не потребуются лишние костыли с java.io.*
// Storage Access Framework?

import android.content.Intent
import android.os.Environment
import java.io.*
import java.util.*
import kotlin.*

const val dir_name = "redcode";

// ProgramFile используется для представления основных файлов, хранимых в папке приложения
data class ProgramFile (val name: String, val last_edit: Long, val size: Long);

class ProgramFileManager {

    // Метод создает директорию "redcode/" (мспользуется в случае, если директория отсутствует)
    private fun initializeRedcodeDirectory() {
        File("./$dir_name").mkdir()
    }

    // Метод используется для отображения файлов, которые находятся в поддиректории приложения "redcode/"

    fun listProgramFiles() : Array<ProgramFile>? {

        val redcodeDir = File("./$dir_name")

        if(!redcodeDir.exists()) {
            redcodeDir.mkdir()
//            return null
        }

        var programFiles = arrayOf<ProgramFile>()

        val foundFiles: Array<out File>? = redcodeDir.listFiles()

        if (foundFiles != null) {
            for (redcodeFile in foundFiles)
            {
                val pf = ProgramFile(
                    redcodeFile.name,
                    redcodeFile.lastModified(),
                    redcodeFile.totalSpace
                )

                programFiles += pf
            }
        }

        return programFiles
    }

    // Метод используется для сохранения программы из редактора в папку "redcode/"
    fun saveProgramFile(nameUri: String, content: String) {
        //если (!файл(name).существует())
        //    файл(name).создать()
        //файлопоток(name, "r").записать(content)
        val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {  }
    }

    fun readProgramFile(nameUri: String) {

    }
}