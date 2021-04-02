package com.example.corewarclone.editorActivity

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.DialogFragment
import com.example.corewarclone.R
import com.example.corewarclone.mainActivity.ProgramFileManager
import com.example.corewarclone.memoryArrayActivity.translator.Translator

const val ACTION_CHOOSE_REDCODE_FILE = 0xbeef

class EditorActivity : AppCompatActivity() {
    private val programFileManager = ProgramFileManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editor)

        selectToolbarTitle()
        val textProcessor = findViewById<TextProcessor>(R.id.text_processor)
        if(intent.dataString != null) {
            textProcessor.text = SpannableStringBuilder(
                programFileManager.readProgramFile(intent.dataString!!))
        }
    }

    private fun selectToolbarTitle() {
        // TODO Выбрать титул в зависимости от наличия/отсутствия файла
        val toolbar = findViewById<Toolbar>(R.id.editor_toolbar)
        val fileName = intent.dataString

        if(fileName != null)
        {
            toolbar.title = fileName
        }
        else
        {
            toolbar.title = resources.getString(R.string.default_title)
        }
        intent.data = Uri.parse(toolbar.title as String)
        setSupportActionBar(toolbar)
    }

    override fun onRestart() {
        super.onRestart()
        selectToolbarTitle()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.editor_menu, menu)
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // TODO Сделать вызов MemoryArrayActivity, если второй файл валиден
        if(requestCode == ACTION_CHOOSE_REDCODE_FILE && resultCode == RESULT_OK) {
            // TODO Сделать/найти другой метод для получения имени файла из Uri
            val dirString = programFileManager.getDirectoryPathFromUri(this, data?.data!!)

            if(dirString != null)
            {
                if(dirString.endsWith(".red"))
                {
                    // Выполнение трансляции второй программы и последующий вызов MemoryArrayActivity/Диалогового окна с ошибкой
                    // Может, лучше часть этого оставить за пределами этого метода?
                    val translator = Translator()
                    translator.translate(dirString)
                }
            }
        }
    }

    private fun saveFile (sourceCode: String) {
        val fileName = intent.data
        if(fileName == null)
        {
            val dialogFragment = ProgramFileDialogFragment(sourceCode)
            dialogFragment.show(supportFragmentManager, "save_file")
        }
        else
        {
            val toolbar = findViewById<Toolbar>(R.id.editor_toolbar)
            //if(toolbar.title != resources.getString(R.string.default_title))
            //{
              //  programFileManager.saveProgramFile(toolbar.title.toString(), sourceCode)
            //}
            //else {
                programFileManager.saveProgramFile(fileName.toString(), sourceCode)
            //}
        }
    }

    override fun onOptionsItemSelected(item: MenuItem) : Boolean {
        val sourceCode = findViewById<TextProcessor>(R.id.text_processor).text.toString()
        if(sourceCode.isBlank())
            return true

        // TODO Выяснить, почему при сохранении нового файла на основе старого перезаписывается старый
        when (item.itemId) {
            R.id.run_menu_item -> {
                // Попробуй сохранять .rbin файлы в локальную папку (как current_dir.txt).
                // Тогда не придется мучиться с URI

                // Еще раз, каков порядок работы приложения:
                // 1. Проверка первого файла на наличие ошибок. Если они есть, выводим диалоговое окно с описанием ошибки
                // 2. Запуск Intent.ACTION_OPEN_DOCUMENT. Если выбранный файл не содержит расширения .red,
                // выводим диалоговое окно, чтобы предупредить о невалидном расширении
                // 3. Компиляция второго файла. Если второй файл не скомпилирован, выводим диалоговое окно
                // с описанием ошибки (желательно пометить, что проблема именно со вторым файлом, чтобы не спутать
                // 4. Запуск MemoryArrayActivity

                val fileName = intent.data
                val translator = Translator()
                val firstFileResult = translator.translate(fileName.toString())
                if(firstFileResult != null)
                {
                    val errorDialog = ProgramFileErrorDialogFragment(fileName.toString(), firstFileResult)
                    errorDialog.show(supportFragmentManager, "error_message")
                    return true
                }

                //val secondFileIntent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                //    type = "*/*"
                //    addCategory(Intent.CATEGORY_OPENABLE)
                //}
                //startActivityForResult(secondFileIntent, ACTION_CHOOSE_REDCODE_FILE)
                return true
            }

           R.id.save_menu_item -> {
                saveFile(sourceCode)
                selectToolbarTitle()
                return true
            }

            R.id.save_as_menu_item -> {
                val dialogFragment = ProgramFileDialogFragment(sourceCode)
                dialogFragment.show(supportFragmentManager, "save_file")
                selectToolbarTitle()
                return true
            }

            R.id.binary_menu_item -> {
                saveFile(sourceCode)
                val fileName = intent.data
                val translator = Translator()
                val assembledProgram = translator.showBytes(fileName.toString())
                if(assembledProgram != null) {
                    val dialogFragment = AssembledProgramDialogFragment(assembledProgram)
                    dialogFragment.show(supportFragmentManager, "assembled_file")
                }
                return true
            }

            R.id.delete_menu_item -> {
                val dialogFragment = DeleteDialogFragment(intent.data.toString())
                dialogFragment.show(supportFragmentManager, "delete_file")
                return true
            }

            else ->{
                super.onOptionsItemSelected(item)
                return true
            }
        }
    }
}