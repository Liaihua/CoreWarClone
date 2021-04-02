package com.example.corewarclone.editorActivity

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.example.corewarclone.R
import com.example.corewarclone.memoryArrayActivity.translator.INSTRUCTION_BYTES_COUNT
import kotlinx.android.synthetic.main.assembled_program_dialog.*
import java.util.*

class AssembledProgramDialogFragment(private val assembled_program: ByteArray) : DialogFragment(){
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        retainInstance = true
        return activity.let {
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater
            val inflatedView = inflater.inflate(R.layout.assembled_program_dialog, null)

            builder.setMessage("Assembled view")
                .setView(inflatedView)
                .setPositiveButton("OK", DialogInterface.OnClickListener { dialogInterface, i ->  })

            val textView = inflatedView.findViewById<TextView>(R.id.assembled_program_text_view)
            if(textView != null)
            {
                textView.text = prettyFormatByteArray(assembled_program)
            }
            builder.create()
        }
    }

    private fun prettyFormatByteArray(array: ByteArray) : String {
        var arrayText = ""
        var byteCount = 0
        var instructionCount = 0
        while (byteCount < array.size)
        {
            instructionCount++
            var line = "${instructionCount}:\t"
            line += "${array[byteCount].toString(16)}\t"
            line += "${array.copyOfRange(byteCount + 1, byteCount + 5).map { it.toUByte().toString(16).toUpperCase(Locale.ROOT) }}\t"
            line += "${array.copyOfRange(byteCount + 5, byteCount + 9).map { it.toUByte().toString(16).toUpperCase(Locale.ROOT) }}"
            arrayText += line + "\n"
            byteCount += INSTRUCTION_BYTES_COUNT
        }
        return arrayText
    }
}
