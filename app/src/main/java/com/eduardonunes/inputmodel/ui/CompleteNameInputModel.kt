package com.eduardonunes.inputmodel.ui

import android.text.method.TextKeyListener
import android.view.inputmethod.EditorInfo
import com.eduardonunes.inputmodel.R
import com.eduardonunes.inputmodel.infrastructure.FieldModelInterface
import com.eduardonunes.inputmodel.infrastructure.FieldTextChangeCallback
import com.eduardonunes.inputmodel.infrastructure.InputFieldState

const val cardHolderMinNames = 2
const val cardholderNameMinLength = 2
const val cardholderNameMaxLength = 26

class CompleteNameInputModel(changeCallback: FieldTextChangeCallback) : FieldModelInterface {
    override val mask: String? = null
    override val capitalize: TextKeyListener.Capitalize? = TextKeyListener.Capitalize.WORDS
    override val labelRes: Int = R.string.nome_exemplo
    override val hintTextRes: Int = R.string.seu_nome
    override val helperTextRes: Int = R.string.nome_completo
    override val inputType: Int = EditorInfo.TYPE_TEXT_VARIATION_PERSON_NAME
    override val maxLength: Int = 8
    override var onTextChangeCallback: FieldTextChangeCallback? = changeCallback


    override fun validateInput(text: String): Pair<InputFieldState, Int?> {
        return when {
            text.trim().hasInvalidChars() -> {
                onTextChangeCallback?.invoke(text, false)
                InputFieldState.INVALID to R.string.nome_invalido
            }
            !text.isFullName() -> {
                onTextChangeCallback?.invoke(text, false)
                InputFieldState.INVALID to R.string.complete_esse_dado
            }
            else -> {
                onTextChangeCallback?.invoke(text, true)
                InputFieldState.VALID to helperTextRes
            }
        }
    }
}

fun String.hasInvalidChars(): Boolean =
    !this.matches("[A-Za-zÀ-ÖØ-öø-ÿ\\s]*".toRegex())

fun String.isFullName(): Boolean {
    val names = trim().split(" ")

    return names.size >= cardHolderMinNames &&
            names.first().length >= cardholderNameMinLength &&
            names.last().length >= cardholderNameMinLength
}