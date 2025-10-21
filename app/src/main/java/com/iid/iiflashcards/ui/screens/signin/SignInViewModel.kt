package com.iid.iiflashcards.ui.screens.signin

import androidx.lifecycle.ViewModel
import com.iid.iiflashcards.util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow(SignInState())
    val state = _state.asStateFlow()

    val event = SingleLiveEvent<Event>()

    fun getUser() = _state.value.userData
    fun signIn() {
        event.value = Event.SignIn
    }

    fun signOut() {
        event.value = Event.SignOut
    }

    fun setUser(userData: UserData?) {
        _state.value = _state.value.copy(
            userData = userData,
            isSignInSuccessful = userData != null,
        )
    }

    fun resetState() {
        _state.update { SignInState() }
    }

    sealed class Event {
        object SignIn : Event()
        object SignOut : Event()
    }
}