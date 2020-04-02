package com.eduardonunes.inputmodel.ui

import android.text.method.TextKeyListener
import android.view.inputmethod.EditorInfo
import com.eduardonunes.inputmodel.R
import com.eduardonunes.inputmodel.infrastructure.FieldModelInterface
import com.eduardonunes.inputmodel.infrastructure.FieldTextChangeCallback
import com.eduardonunes.inputmodel.infrastructure.InputFieldState

const val minNames = 2
const val nameMinLength = 2
const val nameMaxLength = 120

class FullNameFieldModel(changeCallback: FieldTextChangeCallback) : FieldModelInterface {
    override val mask: String? = null
    override val capitalize: TextKeyListener.Capitalize? = TextKeyListener.Capitalize.WORDS
    override val labelRes: Int = R.string.seu_nome
    override val hintTextRes: Int = R.string.nome_exemplo
    override val helperTextRes: Int = R.string.nome_completo
    override val inputType: Int = EditorInfo.TYPE_TEXT_VARIATION_PERSON_NAME
    override val maxLength: Int = nameMaxLength
    override val isRequired: Boolean = true
    override var onTextChangeCallback: FieldTextChangeCallback? = changeCallback

    override fun validateInput(text: String): Pair<InputFieldState, Int?> {
        return when {
            hasInvalidChars(text.trim()) -> {
                onTextChangeCallback?.invoke(text, false)
                InputFieldState.INVALID to R.string.nome_invalido
            }
            !isFullName(text) -> {
                onTextChangeCallback?.invoke(text, false)
                InputFieldState.INVALID to R.string.complete_esse_dado
            }
            else -> {
                onTextChangeCallback?.invoke(text, true)
                InputFieldState.VALID to helperTextRes
            }
        }
    }

    private fun hasInvalidChars(text: String): Boolean =
        !text.matches("[A-Za-zÀ-ÖØ-öø-ÿ\\s]*".toRegex())

    private fun isFullName(text: String): Boolean {
        val names = text.trim().split(" ")

        return names.size >= minNames &&
                names.first().length >= nameMinLength &&
                names.last().length >= nameMinLength
    }
}