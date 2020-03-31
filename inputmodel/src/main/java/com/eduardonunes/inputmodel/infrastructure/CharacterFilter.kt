package com.eduardonunes.inputmodel.infrastructure

import android.text.InputFilter
import android.text.SpannableString
import android.text.Spanned
import android.text.TextUtils

object CharacterFilter {
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
            return regex.matches("" + character)
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
