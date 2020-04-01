package com.eduardonunes.inputmodel.infrastructure

import android.text.Editable
import android.text.TextWatcher
import com.eduardonunes.inputmodel.extensions.applyMask
import com.eduardonunes.inputmodel.extensions.unmask

class MaskTextWatcher(private val mask: String) : TextWatcher {
    private var isRunning = false
    private var isDeleting = false

    override fun beforeTextChanged(charSequence: CharSequence, start: Int, count: Int, after: Int) {
        isDeleting = count > after
    }

    override fun onTextChanged(charSequence: CharSequence, start: Int, before: Int, count: Int) {}

    override fun afterTextChanged(editable: Editable) {
        if (isRunning || isDeleting) {
            return
        }

        isRunning = true

        var index = 0
        for (m in editable.toString().trim().unmask().applyMask(mask).toCharArray()) {
            try {
                if (editable[index] != m) {
                    editable.insert(index, m.toString())
                }
            } catch (e: Exception) {
                break
            }
            index++
        }

        isRunning = false
    }
}