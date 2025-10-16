package com.iid.iiflashcards.ui.screens.deckreview

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class DeckReviewViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(UIState())
    val uiState: StateFlow<UIState> = _uiState

    fun onEvent(event: Event) {
        when (event) {
            is Event.OnReveal -> onReveal()
        }
    }

    private fun onReveal() {
        _uiState.value = _uiState.value.copy(
            isCardExpanded = !_uiState.value.isCardExpanded
        )
    }
}

data class UIState(
    val isCardExpanded: Boolean = true,
)

sealed class Event {
    data object OnReveal : Event()
}