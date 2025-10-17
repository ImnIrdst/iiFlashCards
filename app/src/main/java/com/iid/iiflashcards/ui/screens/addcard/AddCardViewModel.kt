package com.iid.iiflashcards.ui.screens.addcard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iid.iiflashcards.data.model.Card
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

    fun onEvent(event: AddCardEvent) {
        _uiState.value = when (event) {
            is AddCardEvent.OnFrontChange -> _uiState.value.copy(front = event.text)
            is AddCardEvent.OnFrontHintChange -> _uiState.value.copy(frontHint = event.text)
            is AddCardEvent.OnBackChange -> _uiState.value.copy(back = event.text)
            is AddCardEvent.OnBackHintChange -> _uiState.value.copy(backHint = event.text)
            is AddCardEvent.OnSave -> {
                saveCard()
                _uiState.value
            }
        }
    }

    private fun saveCard() {
        viewModelScope.launch {
            val card = Card(
                front = _uiState.value.front,
                frontHint = _uiState.value.frontHint,
                back = _uiState.value.back,
                backHint = _uiState.value.backHint
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

sealed class AddCardEvent {
    data class OnFrontChange(val text: String) : AddCardEvent()
    data class OnFrontHintChange(val text: String) : AddCardEvent()
    data class OnBackChange(val text: String) : AddCardEvent()
    data class OnBackHintChange(val text: String) : AddCardEvent()
    data object OnSave : AddCardEvent()
}