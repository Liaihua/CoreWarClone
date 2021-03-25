package com.example.corewarclone.editorActivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import com.example.corewarclone.R
import com.example.corewarclone.mainActivity.ProgramFileManager
import com.google.android.material.appbar.AppBarLayout

class EditorActivity : AppCompatActivity() {
    private val programFileManager = ProgramFileManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editor)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.editor_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.run_menu_item -> {
            true
        }

        R.id.save_menu_item -> {
            val programText = findViewById<TextProcessor>(R.id.text_processor).text
            if(programText.isNullOrEmpty())
                true
            // Здесь должна быть сохранялка, но мне нужно передать ссылку на экземпляр ProgramFileManager
            true
        }

        else -> super.onOptionsItemSelected(item)
    }
}