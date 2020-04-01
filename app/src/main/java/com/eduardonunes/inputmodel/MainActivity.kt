package com.eduardonunes.inputmodel

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.eduardonunes.inputmodel.ui.FullNameFieldModel
import com.eduardonunes.inputmodel.ui.PhoneNumberFieldModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        inputText?.run {
            inputHasFocus = true
            setInputModel(FullNameFieldModel(::onCompleteNameTextChange))
        }

        inputText2?.run {
            inputHasFocus = false
            setInputModel(PhoneNumberFieldModel(::onPhoneTextChange))
        }
    }

    private fun onCompleteNameTextChange(text: String, isValid: Boolean) {
        Snackbar.make(
            findViewById(R.id.rootView),
            "isValid: $isValid, text: $text",
            Snackbar.LENGTH_SHORT
        ).show()
    }

    private fun onPhoneTextChange(text: String, isValid: Boolean) {
        Snackbar.make(
            findViewById(R.id.rootView),
            "isValid: $isValid, text: $text",
            Snackbar.LENGTH_SHORT
        ).show()
    }
}