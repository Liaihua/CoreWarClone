package com.example.corewarclone.editorActivity

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import com.example.corewarclone.R
import com.example.corewarclone.mainActivity.ProgramFileManager

class ProgramFileDialogFragment(val sourceCode: String): DialogFragment() {
    private val programFileManager = ProgramFileManager
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity.let {
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater
            builder.setMessage(R.string.add_file_dialog_title)
                .setView(inflater.inflate(R.layout.save_program_file_dialog, null))
                .setPositiveButton(R.string.add_file_dialog_positive,
                    DialogInterface.OnClickListener { dialog, id ->
                        val fileName = getDialog()?.window?.findViewById<EditText>(R.id.file_name_edittext)?.text
                        if(fileName != null)
                            programFileManager.saveProgramFile(fileName.toString(), sourceCode)
                    })
                .setNegativeButton(R.string.dialog_negative,
                    DialogInterface.OnClickListener { dialog, id ->
                        getDialog()?.cancel()
                    })
            builder.create()
        }
    }
}