package com.example.corewarclone

import java.util.*
import java.io.* // Для работы с файлами/директориями. Не знаю, нужна ли мне, или нет
import kotlin.*
// ProgramFile используется для представления основных файлов, хранимых в папке приложения
data class ProgramFile (val name: String, val last_edit: Long, val size: Int);

class ProgramFileManager {

    // Метод создает директорию "redcode/" (мспользуется в случае, если директория отсутствует)
    fun initializeRedcodeDirectory() {

    }

    // Метод используется для отображения файлов, которые находятся в поддиректории приложения "redcode/"
    fun listProgramFiles() : List<ProgramFile> {
        return listOf(ProgramFile("test", Date().time, 1))
    }

    // Метод используется для сохранения программы из редактора в папку "redcode/"
    fun saveProgramFile() {

    }
}