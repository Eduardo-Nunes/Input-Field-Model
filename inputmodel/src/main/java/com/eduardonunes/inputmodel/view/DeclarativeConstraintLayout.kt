package com.eduardonunes.inputmodel.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout

abstract class DeclarativeConstraintLayout(context: Context?, attrs: AttributeSet?) :
    ConstraintLayout(context, attrs) {

    abstract fun onCreateView()
    abstract fun afterViewCreated()
    abstract fun onResumeView()
    abstract fun onPauseView()
    abstract fun onDestroyView()

    override fun onVisibilityChanged(changedView: View, visibility: Int) {
        super.onVisibilityChanged(changedView, visibility);
        if (visibility == View.VISIBLE) onResumeView()
        else onPauseView()
    }

    override fun onWindowFocusChanged(hasWindowFocus: Boolean) {
        super.onWindowFocusChanged(hasWindowFocus);
        if (hasWindowFocus) onResumeView()
        else onPauseView()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        onCreateView()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        onDestroyView()
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        afterViewCreated()
    }
}