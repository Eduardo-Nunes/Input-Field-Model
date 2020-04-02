package com.eduardonunes.inputmodel

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by lazy {
        ViewModelProviders.of(this).get(MainViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initData()
        initViews()
    }

    private fun initViews() {
        userNameInputText?.run {
            inputHasFocus = true
            inputtedText = viewModel.nameFieldValue()
            setInputModel(viewModel.usernameFieldModel)
        }
        phoneInputText?.run {
            inputHasFocus = false
            inputtedText = viewModel.phoneFieldValue()
            setInputModel(viewModel.phoneFieldModel)
        }
    }

    private fun initData() = with(viewModel) {
        userNameFieldValue.observe(this@MainActivity, Observer {
            onCompleteNameTextChange(text = it.first, isValid = it.second)
        })
        phoneFieldValue.observe(this@MainActivity, Observer {
            onPhoneTextChange(text = it.first, isValid = it.second)
        })
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