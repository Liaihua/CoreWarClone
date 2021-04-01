package com.example.corewarclone.mainActivity

// java.nio.file недоступен из-за того, что версия API, в которой он есть - 26 (Oreo)
// Повезло повезло

// https://developer.android.com/training/data-storage
// Так как я собираюсь сохранять текстовые файлы независимо от состояния приложения (допустим,
// после удаления), то мне следует уделить внимание параграфу "Documents and other files".
// Так, надеюсь, мне не потребуются лишние костыли с java.io.*
// Storage Access Framework?

import android.content.Context
import android.net.Uri
import android.os.Environment
import android.provider.DocumentsContract.getTreeDocumentId
import android.view.WindowManager
import androidx.core.content.ContextCompat
import kotlinx.coroutines.processNextEventInCurrentThread
import java.io.*
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*
import kotlin.*


// ProgramFile используется для представления основных файлов, хранимых в папке приложения
data class ProgramFile(val name: String, val last_edit: Long, val size: Long);

// Следует добавить методы, которые будут сохранять и возвращать сохраняемый путь к директории,
// в которой мы будем хранить файлы, в/из файл/файла. Если файл отсутствует/пустой, то мы возвращаем null

object ProgramFileManager {

    // Переменная для хранения папки с файлом, где хранится имя используемой приложением папки для прог
    var contextDir: File? = null
    // Переменная для хранения используемой приложением папки для прог
    var currentDir: String? = null

    fun getDirectoryPathFromUri(context: Context, pathUri: Uri) : String? {
        if(pathUri.scheme == "file")
            return pathUri.path.toString()
        val treeId = getTreeDocumentId(pathUri)

        if (treeId != null) {

            //Timber.d("TreeId -> %s", treeId);
            val paths = treeId.split(":").toTypedArray()
            val type = paths[0]
            val subPath = if (paths.size == 2) paths[1] else ""
            return if ("raw".toLowerCase(Locale.ROOT) == type) {
                treeId.substring(treeId.indexOf(File.separator))
            } else if ("PRIMARY".toLowerCase(Locale.ROOT) == type) {
                Environment.getExternalStorageDirectory().absolutePath + File.separator.toString() + subPath
            } else {
                val path = StringBuilder()
                val pathSegment = treeId.split(":").toTypedArray()
                if (pathSegment.size == 1) {
                    path.append(getRemovableStorageRootPath(context, paths[0]))
                } else {
                    val rootPath: String? = getRemovableStorageRootPath(context, paths[0])
                    path.append(rootPath).append(File.separator).append(pathSegment[1])
                }
                path.toString()
            }
        }
        return null
    }

    private fun getRemovableStorageRootPath(context: Context, storageId: String): String? {
        val rootPath = java.lang.StringBuilder()
        val externalFilesDirs = ContextCompat.getExternalFilesDirs(context, null)
        for (fileDir in externalFilesDirs) {
            if (fileDir.path.contains(storageId)) {
                val pathSegment: List<String> = fileDir.path.split(File.separator)
                for (segment in pathSegment) {
                    if (segment == storageId) {
                        rootPath.append(storageId)
                        break
                    }
                    rootPath.append(segment).append(File.separator)
                }
                //rootPath.append(fileDir.getPath().split("/Android")[0]); // faster
                break
            }
        }
        return rootPath.toString()
    }

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
            currentDir = internalFileText
            return internalFileText
        }
    }

    fun listProgramFiles() : Array<ProgramFile>? {

        val redcodeDirPath = loadCurrentDirectory() ?: return null

        val redcodeDir = File(redcodeDirPath)

        // Почитай документацию наконец
        if(!redcodeDir.exists() || !redcodeDir.isDirectory) {
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
                        redcodeFile.length()
                    )

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

    fun readBinaryFile(nameUri: String) : ByteArray {
        val binaryFile = File(currentDir, nameUri)
        return binaryFile.readBytes()
    }

    fun writeBinaryFile(nameUri: String, content: ByteArray) {
        val binaryFile = File(currentDir, nameUri)
        binaryFile.writeBytes(content)
    }

    fun deleteProgramFile(nameUri: String) {
        val programFile = File(currentDir, nameUri)
        programFile.delete()
    }

    fun isExists(nameUri: String) : Boolean {
        val file = File(currentDir, nameUri)
        return file.exists()
    }
}