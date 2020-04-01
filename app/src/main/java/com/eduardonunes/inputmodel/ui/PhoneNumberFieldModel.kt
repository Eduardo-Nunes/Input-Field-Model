package com.eduardonunes.inputmodel.ui

import android.text.InputFilter
import android.text.method.TextKeyListener
import android.view.inputmethod.EditorInfo
import com.eduardonunes.inputmodel.R
import com.eduardonunes.inputmodel.extensions.unmask
import com.eduardonunes.inputmodel.infrastructure.*

private const val CELL_PHONE_MIN_VALID_LENGTH = 4

class PhoneNumberFieldModel(changeCallback: FieldTextChangeCallback) : FieldModelInterface {
    override val mask: String = StringMasks.PHONE.mask
    override val capitalize: TextKeyListener.Capitalize? = TextKeyListener.Capitalize.WORDS
    override val labelRes: Int = R.string.phone_label
    override val hintTextRes: Int = R.string.phone_hint
    override val helperTextRes: Int = R.string.only_numbers
    override val inputType: Int = EditorInfo.TYPE_TEXT_VARIATION_PERSON_NAME
    override val maxLength: Int = mask.length
    override var onTextChangeCallback: FieldTextChangeCallback? = changeCallback

    override fun validateInput(text: String): Pair<InputFieldState, Int?> {
        return when {
            text.unmask().length >= CELL_PHONE_MIN_VALID_LENGTH && !isCellphoneNumber(text.unmask()) -> {
                onTextChangeCallback?.invoke(text, false)
                InputFieldState.INVALID to R.string.phone_invalid
            }
            text.length < maxLength -> {
                onTextChangeCallback?.invoke(text, false)
                InputFieldState.TYPING to helperTextRes
            }
            isCellphoneNumber(text.unmask()) -> {
                onTextChangeCallback?.invoke(text, true)
                InputFieldState.VALID to helperTextRes
            }
            else -> {
                onTextChangeCallback?.invoke(text, false)
                InputFieldState.INVALID to R.string.phone_invalid
            }
        }
    }

    override fun getInputFilters(): MutableList<InputFilter> {
        return super.getInputFilters().apply {
            add(CharacterFilter.fromRegex(Regex(pattern = "[0-9()\\- ]+")))
        }
    }

    private fun isCellphoneNumber(phone: String): Boolean {
        val cellNumberRegex = "^[1-9]{2}9[0-9]{8}\$".toRegex()
        return phone.matches(cellNumberRegex)
    }
}