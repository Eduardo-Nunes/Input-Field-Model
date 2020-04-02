package com.eduardonunes.inputmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.eduardonunes.inputmodel.extensions.copy
import com.eduardonunes.inputmodel.ui.FullNameFieldModel
import com.eduardonunes.inputmodel.ui.PhoneNumberFieldModel

class MainViewModel(application: Application) : AndroidViewModel(application) {

    val userNameFieldValue = MutableLiveData<Pair<String, Boolean>>()

    val usernameFieldModel by lazy {
        FullNameFieldModel { text: String, isValid: Boolean ->
            userNameFieldValue.postValue(text to isValid)
        }
    }

    fun nameFieldValue(): String? {
        return userNameFieldValue.value?.first?.copy()
    }

    val phoneFieldValue = MutableLiveData<Pair<String, Boolean>>()

    val phoneFieldModel by lazy {
        PhoneNumberFieldModel { text: String, isValid: Boolean ->
            phoneFieldValue.postValue(text to isValid)
        }
    }

    fun phoneFieldValue(): String? {
        return phoneFieldValue.value?.first?.copy()
    }

}