package com.eduardonunes.inputmodel.infrastructure

import android.text.InputFilter
import android.text.SpannableString
import android.text.Spanned
import android.text.TextUtils
import android.view.inputmethod.EditorInfo

object CharacterFilter {
    fun isTypeText(inputType: Int): Boolean {
        return inputType == EditorInfo.TYPE_CLASS_TEXT ||
                inputType == EditorInfo.TYPE_TEXT_VARIATION_URI ||
                inputType == EditorInfo.TYPE_TEXT_VARIATION_EMAIL_ADDRESS ||
                inputType == EditorInfo.TYPE_TEXT_VARIATION_EMAIL_SUBJECT ||
                inputType == EditorInfo.TYPE_TEXT_VARIATION_SHORT_MESSAGE ||
                inputType == EditorInfo.TYPE_TEXT_VARIATION_LONG_MESSAGE ||
                inputType == EditorInfo.TYPE_TEXT_VARIATION_PERSON_NAME ||
                inputType == EditorInfo.TYPE_TEXT_VARIATION_POSTAL_ADDRESS ||
                inputType == EditorInfo.TYPE_TEXT_VARIATION_PASSWORD ||
                inputType == EditorInfo.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD ||
                inputType == EditorInfo.TYPE_TEXT_VARIATION_WEB_EDIT_TEXT ||
                inputType == EditorInfo.TYPE_TEXT_VARIATION_FILTER ||
                inputType == EditorInfo.TYPE_TEXT_VARIATION_PHONETIC ||
                inputType == EditorInfo.TYPE_TEXT_VARIATION_WEB_EMAIL_ADDRESS ||
                inputType == EditorInfo.TYPE_TEXT_VARIATION_WEB_PASSWORD ||
                inputType == EditorInfo.TYPE_TEXT_FLAG_CAP_CHARACTERS ||
                inputType == EditorInfo.TYPE_TEXT_FLAG_CAP_WORDS ||
                inputType == EditorInfo.TYPE_TEXT_FLAG_CAP_SENTENCES ||
                inputType == EditorInfo.TYPE_TEXT_FLAG_AUTO_CORRECT ||
                inputType == EditorInfo.TYPE_TEXT_FLAG_AUTO_COMPLETE ||
                inputType == EditorInfo.TYPE_TEXT_FLAG_MULTI_LINE ||
                inputType == EditorInfo.TYPE_TEXT_FLAG_IME_MULTI_LINE ||
                inputType == EditorInfo.TYPE_TEXT_FLAG_NO_SUGGESTIONS ||
                inputType == EditorInfo.TYPE_TEXT_FLAG_CAP_SENTENCES
    }

    fun emoticon(): InputFilter {
        return InputFilter { source, start, end, _, _, _ ->
            for (index in start until end) {
                return@InputFilter when (Character.getType(source[index])) {
                    Character.SURROGATE.toInt(),
                    Character.NON_SPACING_MARK.toInt(),
                    Character.OTHER_SYMBOL.toInt() -> String()
                    else -> null
                }
            }
            null
        }
    }

    fun fromRegex(regex: Regex): InputFilter {
        fun isCharAllowed(character: Char): Boolean {
            return regex.matches("$character")
        }

        return InputFilter { source, start, end, _, _, _ ->
            if (source.isNullOrBlank() || source.isNullOrEmpty()) {
                // Para o backspace manter o original
                return@InputFilter null
            }
            var keepOriginal = true
            val filteredStringBuilder = StringBuilder(end - start)
            for (i in start until end) {
                val character: Char = source[i]
                if (isCharAllowed(character)) filteredStringBuilder.append(character)
                else keepOriginal = false
            }

            if (keepOriginal) null
            else {
                if (source is Spanned) {
                    val spannableString = SpannableString(filteredStringBuilder)
                    TextUtils.copySpansFrom(
                        source,
                        start,
                        filteredStringBuilder.length,
                        null,
                        spannableString,
                        0
                    )
                    spannableString
                } else {
                    filteredStringBuilder
                }
            }
        }
    }
}
