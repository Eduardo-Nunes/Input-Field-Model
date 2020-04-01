package com.eduardonunes.inputmodel.view

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import com.eduardonunes.inputmodel.R
import com.eduardonunes.inputmodel.extensions.changeOrHideImage
import com.eduardonunes.inputmodel.extensions.convertDpToPx
import com.eduardonunes.inputmodel.view.base.FieldModelView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

private const val INPUT_TEXT_PADDING_WITH_ICON = 36

class InputModelView(context: Context, attrs: AttributeSet) : FieldModelView(context, attrs) {

    private lateinit var fieldTrailingIcon: ImageView

    init {
        inflate(context, R.layout.input_model_view, this)
    }

    override fun afterViewCreated() {
        fieldTrailingIcon = findViewById(R.id.fieldTrailingIcon)
        super.afterViewCreated()
    }

    override fun getTextInputEditText(): TextInputEditText? {
        return findViewById(R.id.fieldInputText)
    }

    override fun getTextInputLayout(): TextInputLayout? {
        return findViewById(R.id.fieldInputLayout)
    }

    override fun onTypingState() {
        if (lastValidState == true) {
            fieldTrailingIcon.changeOrHideImage(null)
            setPaddingRightOnInputText(false)
        }
        super.onTypingState()
    }

    private fun setPaddingRightOnInputText(hasIcon: Boolean) {
        getTextInputEditText()?.run {
            setPadding(
                paddingLeft,
                paddingTop,
                if (hasIcon) convertDpToPx(INPUT_TEXT_PADDING_WITH_ICON) else paddingLeft,
                paddingBottom
            )
        }
    }

    override fun onDefaultState() {
        if (lastValidState == true) {
            fieldTrailingIcon.changeOrHideImage(null)
            setPaddingRightOnInputText(false)
        }
        super.onDefaultState()
    }

    override fun onValidState() {
        super.onValidState()
        fieldTrailingIcon.changeOrHideImage(R.drawable.ic_check)
        setPaddingRightOnInputText(true)
    }

    override fun onInvalidState() {
        super.onInvalidState()
        if (inputtedText?.isNotEmpty() == true) {
            fieldTrailingIcon.changeOrHideImage(R.drawable.ic_attention_icon)
        }
        setPaddingRightOnInputText(true)
    }

    override fun onErrorState() {
        super.onErrorState()
        fieldTrailingIcon.changeOrHideImage(R.drawable.ic_error)
        setPaddingRightOnInputText(true)
    }

    override fun cleanError() {
        fieldTrailingIcon.changeOrHideImage(null)
        setPaddingRightOnInputText(false)
        super.cleanError()
    }

}