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
import com.google.android.material.appbar.AppBarLayout
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
        // TODO Выбрать титул в зависимости от наличия/отсутствия файла
        val toolbar = findViewById<Toolbar>(R.id.editor_toolbar)
        val fileName = intent.dataString

        if(fileName != null)
        {
            toolbar.title = fileName
        }
        else
        {
            toolbar.title = R.string.default_title.toString()
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
                selectToolbarTitle()
                return true
            }

            R.id.save_as_menu_item -> {
                val dialogFragment = ProgramFileDialogFragment(sourceCode)
                dialogFragment.show(supportFragmentManager, "save_file")
                selectToolbarTitle()
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