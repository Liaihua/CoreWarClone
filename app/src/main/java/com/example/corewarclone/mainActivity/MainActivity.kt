package com.example.corewarclone.mainActivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.example.corewarclone.R
import kotlin.random.Random
import kotlin.random.nextInt

class MainActivity : AppCompatActivity() {
    private val programFileManager: ProgramFileManager = ProgramFileManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val programFileList = programFileManager.listProgramFiles()
        val programFileAdapter = ProgramFileAdapter(programFileList)
        var pfRecyclerView = findViewById<RecyclerView>(R.id.programs_recycler_view)
        pfRecyclerView.adapter = programFileAdapter
    }

    fun newFile(view: View) {
        var button = view.findViewById<Button>(R.id.fab)
        button.text = Random.nextInt(100).toString()
    }
}