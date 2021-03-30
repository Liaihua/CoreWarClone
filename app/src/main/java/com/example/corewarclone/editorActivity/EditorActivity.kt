package com.example.corewarclone.editorActivity

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.SpannableStringBuilder
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.DialogFragment
import com.example.corewarclone.R
import com.example.corewarclone.mainActivity.MainActivity
import com.example.corewarclone.mainActivity.ProgramFileManager
import kotlinx.android.synthetic.main.program_file_item.*

class EditorActivity : AppCompatActivity() {
    private val programFileManager = ProgramFileManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editor)
        setSupportActionBar(findViewById(R.id.editor_toolbar))

        selectToolbarTitle()
        val textProcessor = findViewById<TextProcessor>(R.id.text_processor)
        if(intent.dataString != null) {
            textProcessor.text = SpannableStringBuilder(
                programFileManager.readProgramFile(intent.dataString!!))
        }
    }

    private fun selectToolbarTitle() {
        val toolbar = findViewById<Toolbar>(R.id.editor_toolbar)
        val fileName = intent.dataString

        if(fileName != null)
        {
            toolbar.title = fileName
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.editor_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) : Boolean {
        val sourceCode = findViewById<TextProcessor>(R.id.text_processor).text.toString()
        if(sourceCode.isBlank())
            return true

        when (item.itemId) {
            R.id.run_menu_item -> {
                // Вызов MemoryArrayActivity
                return true
            }

           R.id.save_menu_item -> {
               // TODO Сделать в методе проверку на наличие файла в папке
               // TODO Добавь в конце концов расширение в шаблоне имени файла
                val fileName = intent.data
                if(fileName == null)
                {
                    val dialogFragment = ProgramFileDialogFragment(sourceCode)
                    dialogFragment.show(supportFragmentManager, "save_file")
                }
                else
                {
                    programFileManager.saveProgramFile(fileName.toString(), sourceCode)
                }
                // Работу этого кода не проверял
                return true
            }

            R.id.save_as_menu_item -> {
                // Добавь диалоговое окно
                val dialogFragment = ProgramFileDialogFragment(sourceCode)
                dialogFragment.show(supportFragmentManager, "save_file")
                return true
            }

            R.id.delete_menu_item -> {
                val dialogFragment = DeleteDialogFragment(intent.data.toString())
                // dialogFragment.showDialog()
                // if(intent.data != null)
                // if(dialogFragment.result == RESULT_OK)
                // programFileManager.deleteProgramFile(file)
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