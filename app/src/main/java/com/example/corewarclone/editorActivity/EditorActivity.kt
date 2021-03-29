package com.example.corewarclone.editorActivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import com.example.corewarclone.R
import com.example.corewarclone.mainActivity.ProgramFileManager

class EditorActivity : AppCompatActivity() {
    private val programFileManager = ProgramFileManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editor)
        setSupportActionBar(findViewById(R.id.editor_toolbar))

        selectToolbarTitle()
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
        val sourceCode = findViewById<TextProcessor>(R.id.text_processor).text
        when (item.itemId) {
            R.id.run_menu_item -> {
                if(sourceCode.isNullOrEmpty())
                    true
                // Вызов MemoryArrayActivity
                true
            }

           R.id.save_menu_item -> {
               // TODO Сделать в методе проверку на наличие файла в папке
               // TODO Добавь в конце концов расширение в шаблоне имени файла
                val fileName = intent.data
                if(sourceCode.isNullOrEmpty())
                    true
                if(fileName == null)
                {
                    val dialogFragment = ProgramFileDialogFragment(sourceCode!!)
                    dialogFragment.show(supportFragmentManager, "save_file")
                }
                else
                {
                    programFileManager.saveProgramFile(fileName.toString(), sourceCode.toString())
                }
                // Работу этого кода не проверял
                true
            }

            R.id.save_as_menu_item -> {
                if(sourceCode.isNullOrEmpty())
                    true
                // Добавь диалоговое окно
                var dialogFragment = ProgramFileDialogFragment(sourceCode!!)
                dialogFragment.show(supportFragmentManager, "save_file")
                true
            }

            else ->{
                super.onOptionsItemSelected(item)
                true
            }
        }
        return true
    }
}