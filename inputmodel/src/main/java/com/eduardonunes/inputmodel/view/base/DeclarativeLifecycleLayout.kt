package com.eduardonunes.inputmodel.view.base

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout

abstract class DeclarativeLifecycleLayout(context: Context, attrs: AttributeSet?) :
    FrameLayout(context, attrs) {

    protected open fun onCreate() = Unit
    protected open fun afterViewCreated() = Unit
    protected open fun onResumeView() = Unit
    protected open fun onPauseView() = Unit
    protected open fun onDestroy() = Unit
    protected open fun onStart() = Unit
    protected open fun onStop() = Unit

    override fun onVisibilityChanged(changedView: View, visibility: Int) {
        super.onVisibilityChanged(changedView, visibility)
        if (visibility == View.VISIBLE) onStart()
        else onStop()
    }

    override fun onWindowFocusChanged(hasWindowFocus: Boolean) {
        super.onWindowFocusChanged(hasWindowFocus)
        if (hasWindowFocus) onResumeView()
        else onPauseView()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        onCreate()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        onDestroy()
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        afterViewCreated()
    }
}