package com.eduardonunes.inputmodel.extensions

import android.text.Editable

fun String.unmask(): String {
    return replace("[.]".toRegex(), String()).replace("[-]".toRegex(), String()).replace("[/]".toRegex(), String())
        .replace("[(]".toRegex(), String()).replace("[ ]".toRegex(), String()).replace("[:]".toRegex(), String())
        .replace("[)]".toRegex(), String())
}

fun String.applyMask(mask: String): String {
    var maskedText = String()
    var i = 0
    for (m in mask.toCharArray()) {
        if (m != '#') {
            maskedText += m
            continue
        }
        try {
            maskedText += this[i]
        } catch (e: Exception) {
            break
        }
        i++
    }
    return maskedText
}

fun String.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this)