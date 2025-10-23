package com.iid.iiflashcards.ui.screens.addcard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iid.iiflashcards.data.model.CardEntity
import com.iid.iiflashcards.data.repository.CardRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddCardViewModel @Inject constructor(
    private val cardRepository: CardRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddCardUiState())
    val uiState: StateFlow<AddCardUiState> = _uiState

    // TODO rename Event to Action
    fun onEvent(event: Event) {
        _uiState.value = when (event) {
            is Event.OnFrontChange -> _uiState.value.copy(front = event.text)
            is Event.OnFrontHintChange -> _uiState.value.copy(frontHint = event.text)
            is Event.OnBackChange -> _uiState.value.copy(back = event.text)
            is Event.OnBackHintChange -> _uiState.value.copy(backHint = event.text)
            is Event.OnSave -> {
                saveCard()
                _uiState.value
            }
        }
    }

    private fun saveCard() {
        viewModelScope.launch {
            val card = CardEntity(
                front = _uiState.value.front,
                frontHint = _uiState.value.frontHint,
                back = _uiState.value.back,
                backHint = _uiState.value.backHint,
            )
            cardRepository.saveCard(card)
        }
    }
}

data class AddCardUiState(
    val front: String = "",
    val frontHint: String = "",
    val back: String = "",
    val backHint: String = "",
)

sealed class Event {
    data class OnFrontChange(val text: String) : Event()
    data class OnFrontHintChange(val text: String) : Event()
    data class OnBackChange(val text: String) : Event()
    data class OnBackHintChange(val text: String) : Event()
    data object OnSave : Event()
}