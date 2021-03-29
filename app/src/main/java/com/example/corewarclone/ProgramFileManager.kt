package com.example.corewarclone.mainActivity

// java.nio.file недоступен из-за того, что версия API, в которой он есть - 26 (Oreo)
// Повезло повезло

// https://developer.android.com/training/data-storage
// Так как я собираюсь сохранять текстовые файлы независимо от состояния приложения (допустим,
// после удаления), то мне следует уделить внимание параграфу "Documents and other files".
// Так, надеюсь, мне не потребуются лишние костыли с java.io.*
// Storage Access Framework?

import android.content.Context
import android.content.Intent
import java.io.*
import kotlin.*

// ProgramFile используется для представления основных файлов, хранимых в папке приложения
data class ProgramFile (val name: String, val last_edit: Long, val size: Long);

// Следует добавить методы, которые будут сохранять и возвращать сохраняемый путь к директории,
// в которой мы будем хранить файлы, в/из файл/файла. Если файл отсутствует/пустой, то мы возвращаем null

object ProgramFileManager {

    // Переменная для хранения папки с файлом, где хранится имя используемой приложением папки для прог
    var contextDir: File? = null
    // Переменная для хранения используемой приложением папки для прог
    var currentDir: String? = null

    fun saveCurrentDirectory(dirPath: String) {
        // Сохраняем в памяти запущенного приложения...
        currentDir = dirPath

        // ... и в постоянном хранилище
        val internalFile = File(contextDir, "current_dir.txt")
        internalFile.writeText(dirPath)
    }

    fun loadCurrentDirectory() : String? {
        // Я проверяю, есть ли файл, в котором написан путь к используемой папке.
        // Если нет, я создаю этот файл и возвращаю null.
        // Затем я смотрю, пустой ли данный файл. Если да, возвращаю null
        // Если все есть, то я загружаю имя папки в currentDir
        if(currentDir != null)
        {
            return currentDir
        }
        else
        {
            val internalFile = File(contextDir, "current_dir.txt") // Замени строку на переменную
            if(!internalFile.exists())
            {
                internalFile.createNewFile()
                return null
            }
            val internalFileText = internalFile.readText()
            if(internalFileText.isBlank())
                return null
            return internalFile.readText()
        }
    }

    fun listProgramFiles() : Array<ProgramFile>? {

        // TODO Проверь, работает ли данный метод на какой-нибудь директории, пока у тебя нет
        //  решения проблемы с Intent.data -> String
        val redcodeDirPath = "/storage/emulated/0/redcode/" //loadCurrentDirectory() ?: return null

        val redcodeDir = File(redcodeDirPath)

        // Почитай документацию наконец
        if(!redcodeDir.exists()) {
            return null
        }

        var programFiles = arrayOf<ProgramFile>()

        val foundFiles = redcodeDir.listFiles()

        if (foundFiles != null) {
            for (redcodeFile in foundFiles)
            {
                if(redcodeFile.extension == "red")
                {
                    val pf = ProgramFile(
                    redcodeFile.name,
                    redcodeFile.lastModified(),
                    redcodeFile.totalSpace)

                    programFiles += pf
                }
            }
        }

        return programFiles
    }

    // Метод используется для сохранения программы из редактора в папку
    fun saveProgramFile(nameUri: String, content: String) {
        val savedProgramFile = File(currentDir, nameUri)
        savedProgramFile.writeText(content, Charsets.UTF_8)
    }

    // Метод для чтения программы из файла
    fun readProgramFile(nameUri: String) : String {
        val programFile = File(currentDir, nameUri)
        return programFile.readText(Charsets.UTF_8)
    }

    fun deleteProgramFile(nameUri: String) {
        val programFile = File(currentDir, nameUri)
        programFile.delete()
    }
}