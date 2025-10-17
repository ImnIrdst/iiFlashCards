package com.iid.iiflashcards.ui.screens.addcard

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AddCardViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(AddCardUiState())
    val uiState: StateFlow<AddCardUiState> = _uiState

    fun onEvent(event: AddCardEvent) {
        _uiState.value = when (event) {
            is AddCardEvent.OnFrontChange -> _uiState.value.copy(front = event.text)
            is AddCardEvent.OnFrontHintChange -> _uiState.value.copy(frontHint = event.text)
            is AddCardEvent.OnBackChange -> _uiState.value.copy(back = event.text)
            is AddCardEvent.OnBackHintChange -> _uiState.value.copy(backHint = event.text)
        }
    }
}

data class AddCardUiState(
    val front: String = "",
    val frontHint: String = "",
    val back: String = "",
    val backHint: String = "",
)

sealed class AddCardEvent {
    data class OnFrontChange(val text: String) : AddCardEvent()
    data class OnFrontHintChange(val text: String) : AddCardEvent()
    data class OnBackChange(val text: String) : AddCardEvent()
    data class OnBackHintChange(val text: String) : AddCardEvent()
}