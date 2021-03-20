package com.example.corewarclone.mainActivity

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.corewarclone.R

class ProgramFileAdapter(private val programFiles: Array<ProgramFile>?) : RecyclerView.Adapter<ProgramFileAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val fileNameTextView: TextView
        val fileLastEditTextView: TextView
        val fileSizeTextView: TextView

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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.program_file_item, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if(programFiles != null)
            holder.bind(programFiles[position])
    }

    override fun getItemCount(): Int {
        if(programFiles == null)
            return 0 // Хм, а у меня не возникнет проблем из-за отсутствия различия
                     // между отсутствием папки и отсутствием файлов в папке?
        return programFiles.size
    }
}
