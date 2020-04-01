package com.eduardonunes.inputmodel.view

import android.content.Context
import android.content.res.ColorStateList
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import com.eduardonunes.inputmodel.R
import com.eduardonunes.inputmodel.extensions.addFilter
import com.eduardonunes.inputmodel.extensions.addOnTextChangeCallback
import com.eduardonunes.inputmodel.extensions.showKeyboard
import com.eduardonunes.inputmodel.infrastructure.CharacterFilter.isTypeText
import com.eduardonunes.inputmodel.infrastructure.FieldInterface
import com.eduardonunes.inputmodel.infrastructure.InputFieldState
import com.eduardonunes.inputmodel.infrastructure.MaskTextWatcher
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.lang.ref.WeakReference

internal const val ON_KEYBOARD_DELAY = 100L
internal const val ON_ERROR_DELAY = 50L

abstract class FieldModelView(context: Context?, attrs: AttributeSet?) :
    DeclarativeConstraintLayout(context, attrs) {

    var inputtedText: String? = null
    var inputHasFocus: Boolean = true
    protected var lastValidState: Boolean? = null
    protected lateinit var _inputModel: FieldInterface
    protected lateinit var _fieldInputLayout: TextInputLayout
    protected lateinit var _fieldInputText: TextInputEditText
    private var maskTextWatcher: MaskTextWatcher? = null
    private lateinit var validateTextWatcher: TextWatcher
    private val onFocusListener = OnFocusChangeListener { v, hasFocus ->
        if (v == _fieldInputText || v == _fieldInputLayout) {
            if (hasFocus) {
                _fieldInputText.postOnAnimationDelayed({
                    _fieldInputText.hint = context?.getString(_inputModel.hintTextRes)
                    _fieldInputText.showKeyboard()
                }, ON_KEYBOARD_DELAY)
            } else {
                _fieldInputText.hint = null
            }
        }
    }

    override fun afterViewCreated() {
        initView()
        initListeners()
        initFocus()
    }

    protected open fun initView() {
        _fieldInputText = getTextInputEditText()
        _fieldInputLayout = getTextInputLayout()
        initDataView()
    }

    abstract fun getTextInputEditText(): TextInputEditText

    abstract fun getTextInputLayout(): TextInputLayout

    private fun initDataView() {
        inputtedText?.run {
            _fieldInputText.setText(this)
        }
        with(_inputModel) {
            _fieldInputLayout.hint = context.getString(labelRes)
            _fieldInputLayout.helperText = context.getString(helperTextRes)
            _fieldInputText.addFilter(getInputFilters())
            setInputType(inputType)
        }
    }

    private fun setInputType(inputType: Int) {
        _fieldInputText.setSelectAllOnFocus(isTypeText(inputType))
        _fieldInputText.inputType = inputType
    }

    protected open fun initListeners() {
        validateTextWatcher = _fieldInputText.addOnTextChangeCallback { newText ->
            fieldViewState = validateInputText(newText)
        }

        _inputModel.getKeyListener()?.run(_fieldInputText::setKeyListener)
        if (_inputModel.mask != null) {
            maskTextWatcher = MaskTextWatcher(_inputModel.mask.toString())
            maskTextWatcher?.run(_fieldInputText::addTextChangedListener)
        }

        _fieldInputText.onFocusChangeListener = onFocusListener
    }

    private fun initFocus() {
        if (inputHasFocus) {
            _fieldInputText.requestFocus()
        }
    }

    override fun onDestroyView() {
        removeListeners()
    }

    private fun removeListeners() {
        _fieldInputText.onFocusChangeListener = null
        _fieldInputText.removeTextChangedListener(validateTextWatcher)
        maskTextWatcher?.run(_fieldInputText::removeTextChangedListener)
    }

    internal var fieldViewState: Pair<InputFieldState, Int?> = InputFieldState.DEFAULT to null
        set(value) {
            if (value != field) {
                field = value
                invalidateState()
            }
        }

    protected open fun invalidateState() =
        when (fieldViewState.first) {
            InputFieldState.DEFAULT -> onDefaultState()
            InputFieldState.VALID -> onValidState()
            InputFieldState.INVALID -> onInvalidState()
            InputFieldState.TYPING -> onTypingState()
            InputFieldState.EMPTY -> onEmptyState()
            InputFieldState.ERROR -> onErrorState()
        }


    protected open fun onDefaultState() {
        if (lastValidState != null && !lastValidState!!) {
            cleanError()
        }
        _fieldInputText.hint = null
        lastValidState = null
    }

    protected open fun onValidState() {
        if (lastValidState != null && !lastValidState!!) {
            cleanError()
        }
        _fieldInputLayout.helperText = fieldViewState.second?.let { context.getString(it) }
        lastValidState = true
    }

    protected open fun onInvalidState() {
        if (_fieldInputText.text?.isEmpty() == true) return cleanError()
        setErrorColor(R.color.yellow)
        setErrorText()
    }

    protected open fun onTypingState() {
        if (lastValidState != null && !lastValidState!!) {
            cleanError()
        }
        lastValidState = null
    }

    protected open fun onEmptyState() {
        lastValidState = null
        _fieldInputText.text?.clear()
    }

    protected open fun onErrorState() {
        setErrorColor(R.color.orange)
        setErrorText()
    }

    private fun setErrorText() {
        val errorTextId = fieldViewState.second ?: _inputModel.helperTextRes
        lastValidState = false
        with(_fieldInputLayout) {
            helperText = null
            postDelayed({
                error = context.getString(errorTextId)
            }, ON_ERROR_DELAY)
        }
    }

    protected open fun cleanError() {
        _fieldInputLayout.error = null
        val helperTextId =
            if (_fieldInputText.inputType != EditorInfo.TYPE_TEXT_VARIATION_PASSWORD) {
                fieldViewState.second ?: _inputModel.helperTextRes
            } else R.string.empty

        _fieldInputLayout.helperText = context.getString(helperTextId)
    }

    private fun setErrorColor(@ColorRes color: Int) = with(_fieldInputLayout) {
        try {
            postDelayed({
                setErrorTextColor(
                    ColorStateList.valueOf(ContextCompat.getColor(context, color))
                )
            }, ON_ERROR_DELAY)
        } catch (e: Exception) {
        }
    }

    private fun validateInputText(validateText: String): Pair<InputFieldState, Int?> {
        return when {
            validateText.isBlank() -> {
                _inputModel.onTextChangeCallback?.invoke(validateText, false)
                InputFieldState.INVALID to R.string.complete_data
            }
            validateText.isEmpty() -> {
                _inputModel.onTextChangeCallback?.invoke(validateText, false)
                InputFieldState.EMPTY to _inputModel.helperTextRes
            }
            isTypeText(_inputModel.inputType) -> {
                _inputModel.validateInput(validateText)
            }
            else -> {
                _inputModel.validateInput(validateText)
            }
        }
    }
}