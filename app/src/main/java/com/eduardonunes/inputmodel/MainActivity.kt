package com.eduardonunes.inputmodel

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.eduardonunes.inputmodel.ui.CompleteNameInputModel
import com.eduardonunes.inputmodel.ui.PhoneNumberInputModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        inputText?.run {
            inputHasFocus = true
            setInputModel(CompleteNameInputModel(::onCompleteNameTextChange))
        }

        inputText2?.run {
            inputHasFocus = false
            setInputModel(PhoneNumberInputModel(::onPhoneTextChange))
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