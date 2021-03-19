package com.example.corewarclone.mainActivity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.corewarclone.R
import com.example.corewarclone.editorActivity.EditorActivity

class MainActivity : AppCompatActivity() {
    private val programFileManager: ProgramFileManager = ProgramFileManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val programFileList = programFileManager.listProgramFiles()
        val programFileAdapter = ProgramFileAdapter(programFileList)
        val pfRecyclerView = findViewById<RecyclerView>(R.id.programs_recycler_view)

        pfRecyclerView.adapter = programFileAdapter
        pfRecyclerView.layoutManager = LinearLayoutManager(this)

        pfRecyclerView.setOnClickListener {
            openFile(it)
        }
    }

    // Для данного метода потребуется создание Intent ACTION_OPEN_DOCUMENT

    fun openFile(view: View) {
        val button = view.findViewById<Button>(R.id.fab)
        button.text = view.id.toString()

        // Данный Intent должен открывать файл, имя которого указано в extra-ресурсе (как я понял)
        val editorIntent = Intent(this, EditorActivity::class.java)
        startActivity(editorIntent)
    }

    // А для этого - ACTION_CREATE_DOCUMENT

    fun newFile(view: View) {
        val editorIntent = Intent(this, EditorActivity::class.java)
        startActivity(editorIntent)
    }
}