package com.example.corewarclone.editorActivity

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.example.corewarclone.R
import java.util.*

// TODO Сделать отображение "скомпилированного" кода
class AssembledProgramDialogFragment(assembled_program: ByteArray) : DialogFragment(){
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity.let {
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater
            builder.setMessage("Assembled view")
                .setView(inflater.inflate(R.layout.assembled_program_dialog, null))
                .setPositiveButton("OK", DialogInterface.OnClickListener { dialogInterface, i ->  })
            //dialog?.
            builder.create()
        }
    }

    fun prettyFormatByteArray(array: ByteArray) : String {
        var arrayText = ""
        var byteCount = 0
        var instructionCount = 0
        while (byteCount < array.size)
        {
            instructionCount++
            var line = "${instructionCount}:\t"
            line += "${array[byteCount].toString(16)} "
            line += "${array.copyOfRange(byteCount + 1, byteCount + 5).map { it.toUByte().toString(16).toUpperCase(Locale.ROOT) }}\t"
            line += "${array.copyOfRange(byteCount + 5, byteCount + 9).map { it.toUByte().toString(16).toUpperCase(Locale.ROOT) }}"
            arrayText += line + "\n"
            byteCount += 9
        }
        return arrayText
    }
}
