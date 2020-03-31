package com.eduardonunes.inputmodel.infrastructure

import android.text.InputFilter
import android.text.method.TextKeyListener
import androidx.annotation.StringRes

interface FieldInterface {
    val mask: String?
    val capitalize: TextKeyListener.Capitalize?

    @get:StringRes
    val labelRes: Int

    @get:StringRes
    val hintTextRes: Int

    @get:StringRes
    val helperTextRes: Int
    val inputType: Int
    val maxLength: Int
    var onTextChangeCallback: FieldTextChangeCallback?
    fun validateInput(text: String): Pair<InputFieldState, Int?>
    fun inputFilters(): MutableList<InputFilter> {
        return mutableListOf(
            InputFilter.LengthFilter(maxLength),
            CharacterFilter.emoticon()
        )
    }

    fun keyListener(): TextKeyListener? {
        return capitalize?.run { TextKeyListener(this, false) }
    }
}