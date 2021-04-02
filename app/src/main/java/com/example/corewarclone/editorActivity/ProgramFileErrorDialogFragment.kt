package com.example.corewarclone.editorActivity

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.example.corewarclone.memoryArrayActivity.translator.Error

class ProgramFileErrorDialogFragment(private val fileName: String, private val error: Error): DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        retainInstance = true
        activity.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle(fileName)
                .setMessage("Error at line: ${error.line}\nDescription:\n${error.description}")
            return builder.create()
        }
    }
}