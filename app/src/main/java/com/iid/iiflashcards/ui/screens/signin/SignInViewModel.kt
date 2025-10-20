package com.iid.iiflashcards.ui.screens.signin

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val googleAuthUiClient: GoogleAuthUiClient,
) : ViewModel() {

    private val _state = MutableStateFlow(SignInState())
    val state = _state.asStateFlow()

    fun getUser() = googleAuthUiClient.getSignedInUser()
    suspend fun signIn() = googleAuthUiClient.signIn()
    suspend fun signOut() = googleAuthUiClient.signOut()

    fun resetState() {
        _state.update { SignInState() }
    }
}