package com.example.corewarclone.editorActivity

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.example.corewarclone.R
import com.example.corewarclone.mainActivity.ProgramFileManager

class DeleteDialogFragment(val programFilePath: String) : DialogFragment(){
    private val programFileManager = ProgramFileManager
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity.let {
            val builder = AlertDialog.Builder(it)
            builder.setMessage(R.string.dialog_delete_program_file)
                .setPositiveButton(R.string.delete_file_dialog_positive,
                    DialogInterface.OnClickListener { dialog, i ->
                        programFileManager.deleteProgramFile(programFilePath)
                        activity?.finish()
                    })
                .setNegativeButton(R.string.dialog_negative,
                    DialogInterface.OnClickListener { dialog, i ->
                        getDialog()?.cancel()
                    })
            builder.create()
        }
    }
}
