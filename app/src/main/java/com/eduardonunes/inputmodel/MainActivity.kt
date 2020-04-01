package com.eduardonunes.inputmodel

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.TextKeyListener
import android.text.method.TextKeyListener.Capitalize
import android.view.inputmethod.EditorInfo
import com.eduardonunes.inputmodel.infrastructure.FieldModelInterface
import com.eduardonunes.inputmodel.infrastructure.FieldTextChangeCallback
import com.eduardonunes.inputmodel.infrastructure.InputFieldState
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }
}
