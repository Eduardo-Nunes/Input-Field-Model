package com.eduardonunes.inputmodel.view.base

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout

abstract class DeclarativeLifecycleLayout(context: Context, attrs: AttributeSet?) :
    FrameLayout(context, attrs) {

    open fun onCreate() = Unit
    open fun afterViewCreated() = Unit
    open fun onResumeView() = Unit
    open fun onPauseView() = Unit
    open fun onDestroy() = Unit
    open fun onStart() = Unit
    open fun onStop() = Unit

    override fun onVisibilityChanged(changedView: View, visibility: Int) {
        super.onVisibilityChanged(changedView, visibility);
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