package com.example.corewarclone.editorActivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import com.example.corewarclone.R

class EditorActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editor)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        println(menu)
        val inflater = menuInflater
        inflater.inflate(R.menu.editor_menu, menu)
        return true
    }
}