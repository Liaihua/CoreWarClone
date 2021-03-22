package com.example.corewarclone.mainActivity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.example.corewarclone.R
import com.example.corewarclone.editorActivity.EditorActivity
import androidx.recyclerview.widget.LinearLayoutManager

class MainActivity : AppCompatActivity() {
    private val programFileManager: ProgramFileManager = ProgramFileManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val programFileList = programFileManager.listProgramFiles()
        val programFileAdapter = ProgramFileAdapter(programFileList) {
            val editorIntent = Intent(this, EditorActivity::class.java).apply {
                // Здесь?
            }
            startActivity(editorIntent)
        }
        val pfRecyclerView = findViewById<RecyclerView>(R.id.programs_recycler_view)

        pfRecyclerView.adapter = programFileAdapter
        pfRecyclerView.layoutManager = LinearLayoutManager(this)
    }

    // Для данного метода потребуется создание Intent ACTION_OPEN_DOCUMENT

    fun openFile(view: View) {
        val editorIntent = Intent(this, EditorActivity::class.java).apply {
            // Extra с файлом??
            println(this.action)

        }
        startActivity(editorIntent)
    }

    // А для этого - ACTION_CREATE_DOCUMENT
    fun newFile(view: View) {
        val editorIntent = Intent(this, EditorActivity::class.java)
        startActivity(editorIntent)
    }
    // Задолбал уже отвлекаться
}