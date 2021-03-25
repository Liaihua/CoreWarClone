package com.example.corewarclone.editorActivity

import android.content.Context
import android.graphics.Color
import android.text.Editable
import android.text.Spannable
import android.text.TextWatcher
import android.text.style.ForegroundColorSpan
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import com.example.corewarclone.R
import java.util.regex.Pattern

class TextProcessor @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = R.attr.editTextStyle) : AppCompatEditText(context, attrs, defStyleAttr) {
    private val KEYWORDS = Pattern.compile("\\b(mov|str)") // продолжи другими opcode'ми
    private val textWatcher: TextWatcher
    init {
        textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, after: Int) {println(s)}
            override fun afterTextChanged(s: Editable?) {
                syntaxHighlight()
            }
        }
    }
    private fun syntaxHighlight() {
        val matcher = KEYWORDS.matcher(text)
        matcher.region(0, text!!.length)
        while(matcher.find()){
            text!!.setSpan(
                ForegroundColorSpan(Color.parseColor("#007f9b")),
                matcher.start(),
                matcher.end(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
    }
    fun processText(newText: String) {
        removeTextChangedListener(textWatcher)
        setText(newText)
        addTextChangedListener(textWatcher)
    }
}