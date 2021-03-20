package com.example.corewarclone.mainActivity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
<<<<<<< HEAD
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.corewarclone.R
import com.example.corewarclone.editorActivity.EditorActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
=======
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.corewarclone.R
import com.example.corewarclone.editorActivity.EditorActivity
>>>>>>> 0c4069e1e6111c122338b8f59ce73a16008ca30d

class MainActivity : AppCompatActivity() {
    private val programFileManager: ProgramFileManager = ProgramFileManager()

<<<<<<< HEAD
        init {
            fileNameTextView = view.findViewById(R.id.file_name)
            fileLastEditTextView = view.findViewById(R.id.file_date_time)
            fileSizeTextView = view.findViewById(R.id.file_size)
        }

        fun bind(pf: ProgramFile) {
            fileNameTextView.text = pf.name
            fileLastEditTextView.text = pf.last_edit.toString()
            fileSizeTextView.text = pf.size.toString()
        }
    }
=======
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
>>>>>>> 0c4069e1e6111c122338b8f59ce73a16008ca30d

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

<<<<<<< HEAD
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val programFileList = programFileManager.listProgramFiles()
        val programFileAdapter = ProgramFileAdapter(programFileList)
        var pf_recyclerView = findViewById<RecyclerView>(R.id.programs_recycler_view)
        pf_recyclerView.adapter = programFileAdapter

        val button = findViewById<Button>(R.id.fab)
        button.setOnClickListener {
            newFile(it)
        }
    }

    // Задолбал уже отвлекаться

    fun newFile(view: View) {
        val intent = Intent(this, EditorActivity::class.java)
        startActivity(intent)
=======
        // Данный Intent должен открывать файл, имя которого указано в extra-ресурсе (как я понял)
        val editorIntent = Intent(this, EditorActivity::class.java)
        startActivity(editorIntent)
    }

    // А для этого - ACTION_CREATE_DOCUMENT

    fun newFile(view: View) {
        val editorIntent = Intent(this, EditorActivity::class.java)
        startActivity(editorIntent)
>>>>>>> 0c4069e1e6111c122338b8f59ce73a16008ca30d
    }
}