package com.example.corewarclone.editorActivity

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment

// TODO Сделать отображение "скомпилированного" кода
class AssembledProgramDialogFragment : DialogFragment(){
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity.let {
            val builder = AlertDialog.Builder(it)
            builder.setMessage("Assembled view")
            builder.create()
        }
    }
}
