package com.eduardonunes.inputmodel.view.base

import android.content.Context
import android.content.res.ColorStateList
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.inputmethod.EditorInfo
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import com.eduardonunes.inputmodel.R
import com.eduardonunes.inputmodel.extensions.*
import com.eduardonunes.inputmodel.infrastructure.CharacterFilter.isTypeText
import com.eduardonunes.inputmodel.infrastructure.FieldModelInterface
import com.eduardonunes.inputmodel.infrastructure.InputFieldState
import com.eduardonunes.inputmodel.infrastructure.MaskTextWatcher
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

internal const val ON_KEYBOARD_DELAY = 100L
internal const val ON_ERROR_DELAY = 50L

private const val STRING_TO_HIDE = " "

abstract class FieldModelView(context: Context, attrs: AttributeSet?) :
    DeclarativeLifecycleLayout(context, attrs) {

    var inputtedText: String? = null
        set(value) {
            if (field != value) {
                fieldInputText?.text = value?.toEditable()
            }
            field = value
        }
        get() = fieldInputText?.text.toString()

    var inputHasFocus: Boolean = false
        set(value) {
            if (field != value) initFocus()
            field = value
        }

    fun setInputModel(model: FieldModelInterface) {
        this._inputModel = model
        setupWithModelData(model)
        setupWithModelListeners(model)
    }

    protected var lastValidState: Boolean? = null
    private var _inputModel: FieldModelInterface? = null
    private var fieldInputLayout: TextInputLayout? = null
    private var fieldInputText: TextInputEditText? = null
    private var maskTextWatcher: MaskTextWatcher? = null
    private var validateTextWatcher: TextWatcher? = null
    private val onFocusListener = OnFocusChangeListener { v, hasFocus ->
        if (v == fieldInputText || v == fieldInputLayout) {
            if (hasFocus) {
                fieldInputText?.postOnAnimationDelayed({
                    fieldInputText?.hint = _inputModel?.hintTextRes?.let { context.getString(it) }
                    fieldInputText?.showKeyboard()
                }, ON_KEYBOARD_DELAY)
            } else {
                fieldInputText?.hint = null
            }
        }
    }

    override fun afterViewCreated() {
        initView()
        initListeners()
        initFocus()
    }

    protected open fun initView() {
        fieldInputText = getTextInputEditText()
        fieldInputLayout = getTextInputLayout()
        initDataView()
    }

    protected abstract fun getTextInputEditText(): TextInputEditText?

    protected abstract fun getTextInputLayout(): TextInputLayout?

    private fun initDataView() {
        inputtedText?.run {
            fieldInputText?.setText(this)
        }
        _inputModel?.run(::setupWithModelData)
    }

    private fun setupWithModelData(model: FieldModelInterface) = with(model) {
        fieldInputLayout?.hint = context.getString(labelRes)
        fieldInputLayout?.helperText = context.getString(helperTextRes)
        fieldInputText?.addFilter(getInputFilters())
        setInputType(inputType)
    }

    private fun setInputType(inputType: Int) {
        fieldInputText?.setSelectAllOnFocus(isTypeText(inputType))
        fieldInputText?.inputType = inputType
    }

    protected open fun initListeners() {
        validateTextWatcher = fieldInputText?.addOnTextChangeCallback { newText ->
            fieldViewState = validateInputText(newText)
        }

        _inputModel?.run(::setupWithModelListeners)

        fieldInputText?.onFocusChangeListener = onFocusListener
    }

    private fun setupWithModelListeners(model: FieldModelInterface) = with(model) {
        getKeyListener()?.run { fieldInputText?.setKeyListener(this) }
        maskTextWatcher = mask?.let { maskPattern ->
            MaskTextWatcher(maskPattern)
        }?.also { maskWatcher ->
            fieldInputText?.addTextChangedListener(maskWatcher)
        }
    }

    private fun initFocus() {
        if (inputHasFocus) {
            fieldInputText?.requestFocus()
        }
    }

    override fun onDestroy() {
        removeListeners()
    }

    private fun removeListeners() {
        fieldInputText?.onFocusChangeListener = null
        fieldInputText?.removeTextChangedListener(validateTextWatcher)
        maskTextWatcher?.run { fieldInputText?.removeTextChangedListener(this) }
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
        if (lastValidState == false) {
            cleanError()
        }
        fieldInputText?.hint = null
        lastValidState = null
    }

    protected open fun onValidState() {
        if (lastValidState == false) {
            cleanError()
        }
        fieldInputLayout?.helperText = fieldViewState.second?.let { context.getString(it) }
        lastValidState = true
    }

    protected open fun onInvalidState() {
        if (fieldInputText?.text?.isEmpty() == true) return cleanError()
        setErrorColor(R.color.yellow)
        setErrorText()
    }

    protected open fun onTypingState() {
        if (lastValidState == false) {
            cleanError()
        }
        lastValidState = null
    }

    protected open fun onEmptyState() {
        lastValidState = null
        fieldInputText?.text?.clear()
    }

    protected open fun onErrorState() {
        setErrorColor(R.color.orange)
        setErrorText()
    }

    private fun setErrorText() {
        val errorTextId = fieldViewState.second ?: _inputModel?.helperTextRes ?: R.string.empty
        lastValidState = false
        fieldInputLayout?.run {
            hideHelper()
            postDelayed({
                error = context.getString(errorTextId)
            }, ON_ERROR_DELAY)
        }
    }

    private fun hideHelper() {
        fieldInputLayout?.helperText = STRING_TO_HIDE
    }


    protected open fun cleanError() {
        hideError()
        val helperTextId =
            if (fieldInputText?.inputType != EditorInfo.TYPE_TEXT_VARIATION_PASSWORD) {
                fieldViewState.second ?: _inputModel?.helperTextRes ?: R.string.empty
            } else R.string.empty

        fieldInputLayout?.helperText = context.getString(helperTextId)
    }

    private fun hideError() {
        fieldInputLayout?.error = STRING_TO_HIDE
    }

    private fun setErrorColor(@ColorRes color: Int) = fieldInputLayout?.run {
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
        val nonNullModel = _inputModel ?: return InputFieldState.DEFAULT to R.string.empty

        return when {
            validateText.isBlank() -> {
                nonNullModel.onTextChangeCallback?.invoke(validateText, false)
                InputFieldState.INVALID to R.string.complete_data
            }
            validateText.isEmpty() -> {
                nonNullModel.onTextChangeCallback?.invoke(validateText, false)
                InputFieldState.EMPTY to nonNullModel.helperTextRes
            }
            isTypeText(nonNullModel.inputType) -> {
                nonNullModel.validateInput(validateText)
            }
            else -> {
                nonNullModel.validateInput(validateText)
            }
        }
    }

}