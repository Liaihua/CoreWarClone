package com.example.corewarclone.mainActivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.corewarclone.R
import java.util.*

class ProgramFileAdapter(private val programFiles: Array<ProgramFile>) : RecyclerView.Adapter<ProgramFileAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val fileNameTextView: TextView
        val fileLastEditTextView: TextView
        val fileSizeTextView: TextView

        init {
            fileNameTextView = view.findViewById(R.id.file_name)
            fileLastEditTextView = view.findViewById(R.id.file_date_time)
            fileSizeTextView = view.findViewById(R.id.file_size)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.program_file_item, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.fileNameTextView.text = programFiles[position].name
        holder.fileSizeTextView.text = programFiles[position].size.toString()
        holder.fileLastEditTextView.text = programFiles[position].last_edit.toString()
    }

    override fun getItemCount(): Int {
        return programFiles.size
    }
}

class MainActivity : AppCompatActivity() {
    val programFileManager: ProgramFileManager = ProgramFileManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val programFileList = programFileManager.listProgramFiles()
        val programFileAdapter = ProgramFileAdapter(programFileList)
        var pf_recyclerView = findViewById<RecyclerView>(R.id.programs_recycler_view)
        pf_recyclerView.adapter = programFileAdapter
    }

    fun newFile(view: View) {

    }
}