package com.eduardonunes.inputmodel.view

import android.content.Context
import android.util.AttributeSet
import com.eduardonunes.inputmodel.R
import com.eduardonunes.inputmodel.view.base.FieldModelView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class InputModelView(context: Context, attrs: AttributeSet) : FieldModelView(context, attrs) {


    override fun onCreateView() {
        inflate(context, R.layout.input_model_view, this)
    }

    override fun getTextInputEditText(): TextInputEditText? {
        return findViewById(R.id.fieldInputText)
    }

    override fun getTextInputLayout(): TextInputLayout? {
        return findViewById(R.id.fieldInputLayout)
    }
}