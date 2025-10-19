package com.iid.iiflashcards.ui.auth

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SignInViewModel : ViewModel() {

    private val _state = MutableStateFlow(SignInState())
    val state = _state.asStateFlow()

    fun onSignInResult(result: SignInResult) {
        _state.update {
            it.copy(
                isSignInSuccessful = result.data != null,
                signInError = result.errorMessage
            )
        }
    }

    fun setSignedIn() {
        _state.value = _state.value.copy(isSignInSuccessful = true)
    }

    fun setSignedOut() {
        _state.value = _state.value.copy(isSignInSuccessful = false, signInError = "signInError")
    }

    fun resetState() {
        _state.update { SignInState() }
    }
}