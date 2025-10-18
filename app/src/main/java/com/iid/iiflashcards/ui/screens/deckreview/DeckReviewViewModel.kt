package com.iid.iiflashcards.ui.screens.deckreview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iid.iiflashcards.data.repository.CardRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class DeckReviewViewModel @Inject constructor(
    cardRepository: CardRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(UIState())
    val uiState: StateFlow<UIState> = _uiState

    init {
        cardRepository.getAllCards().onEach { cards ->
            _uiState.value = UIState(
                cards = cards.map {
                    UIState.Card(
                        front = it.front,
                        frontHint = it.frontHint ?: "...",
                        back = it.back,
                        backHint = it.backHint ?: "...",
                    )
                }
            )
        }.launchIn(viewModelScope)
    }

    fun onEvent(event: Event) {
        when (event) {
            is Event.OnReveal -> onReveal()
        }
    }

    private fun onReveal() {
        _uiState.value = _uiState.value.toggleCardReveal()
    }
}

sealed class Event {
    data object OnReveal : Event()
}

data class UIState(
    val cardIndex: Int = 0,
    val cards: List<Card> = emptyList(),
) {
    data class Card(
        val front: String,
        val frontHint: String,
        val back: String,
        val backHint: String,
        val isExpanded: Boolean = true,
    )
}

fun UIState.toggleCardReveal() = copy(
    cards = cards.mapIndexed { index, card ->
        if (index == cardIndex) {
            card.copy(isExpanded = !card.isExpanded)
        } else {
            card
        }
    }
)

val UIState.currentCard: UIState.Card?
    get() = cards.getOrNull(cardIndex)
val UIState.isCardExpanded: Boolean
    get() = cards.getOrNull(cardIndex)?.isExpanded ?: true
