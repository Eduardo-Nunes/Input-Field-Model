package com.eduardonunes.inputmodel.view.base

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout

abstract class DeclarativeLifecycleLayout(context: Context?, attrs: AttributeSet?) :
    RelativeLayout(context, attrs) {

    open fun onCreateView() = Unit
    open fun afterViewCreated() = Unit
    open fun onResumeView() = Unit
    open fun onPauseView() = Unit
    open fun onDestroyView() = Unit

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