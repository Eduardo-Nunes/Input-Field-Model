package com.eduardonunes.inputmodel.extensions

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import kotlin.math.roundToInt

fun View.showKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
}

fun View.hideKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(this.windowToken, 0)
}

fun View.convertDpToPx(dp: Int): Int = dp.times(context.resources.displayMetrics.density).roundToInt()