package com.eduardonunes.inputmodel.extensions

import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.widget.EditText

fun EditText.addFilter(filters: List<InputFilter>) {
    filters.forEach { this.addFilter(it) }
}

fun EditText.addFilter(filter: InputFilter) {
    filters =
        if (filters.isNullOrEmpty()) {
            arrayOf(filter)
        } else {
            filters
                .toMutableList()
                .apply {
                    removeAll { it.javaClass == filter.javaClass }
                    add(filter)
                }
                .toTypedArray()
        }
}

inline fun EditText.addOnTextChangeCallback(crossinline callback: (String) -> Unit): TextWatcher {
    val textWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) = callback(s.toString())
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    }
    addTextChangedListener(textWatcher)

    return textWatcher
}