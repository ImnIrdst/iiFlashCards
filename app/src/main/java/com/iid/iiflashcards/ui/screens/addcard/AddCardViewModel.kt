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

    fun doAction(action: Action) {
        _uiState.value = when (action) {
            is Action.OnFrontChange -> _uiState.value.copy(front = action.text)
            is Action.OnFrontHintChange -> _uiState.value.copy(frontHint = action.text)
            is Action.OnBackChange -> _uiState.value.copy(back = action.text)
            is Action.OnBackHintChange -> _uiState.value.copy(backHint = action.text)
            is Action.OnSave -> {
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

sealed class Action {
    data class OnFrontChange(val text: String) : Action()
    data class OnFrontHintChange(val text: String) : Action()
    data class OnBackChange(val text: String) : Action()
    data class OnBackHintChange(val text: String) : Action()
    data object OnSave : Action()
}