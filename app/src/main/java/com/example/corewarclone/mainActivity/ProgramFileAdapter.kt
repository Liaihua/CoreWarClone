package com.example.corewarclone.mainActivity

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.corewarclone.R

class ProgramFileAdapter(private val programFiles: Array<ProgramFile>?, val onClick: (ProgramFile) -> Unit) : RecyclerView.Adapter<ProgramFileAdapter.ViewHolder>() {

    class ViewHolder(view: View, val onClick: (ProgramFile) -> Unit) : RecyclerView.ViewHolder(view) {

        var pf: ProgramFile? = null
        val fileNameTextView = view.findViewById<TextView>(R.id.file_name)
        val fileLastEditTextView = view.findViewById<TextView>(R.id.file_date_time)
        val fileSizeTextView = view.findViewById<TextView>(R.id.file_size)

        fun bind(pf: ProgramFile) {
            this.pf = pf
            fileNameTextView.text = pf.name
            fileLastEditTextView.text = pf.last_edit.toString()
            fileSizeTextView.text = pf.size.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.program_file_item, parent, false)

        return ViewHolder(view, onClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if(programFiles != null) {
            holder.bind(programFiles[position])
            holder.itemView.setOnClickListener {
                onClick(programFiles[position])
            }
        }
    }

    override fun getItemCount(): Int {
        if(programFiles == null)
            return 0 // Хм, а у меня не возникнет проблем из-за отсутствия различия
                     // между отсутствием папки и отсутствием файлов в папке?
        return programFiles.size
    }
}
