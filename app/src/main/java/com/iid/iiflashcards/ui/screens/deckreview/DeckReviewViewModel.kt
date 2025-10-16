package com.iid.iiflashcards.ui.screens.deckreview

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class DeckReviewViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(UIState())
    val uiState: StateFlow<UIState> = _uiState

    init {
        _uiState.value = UIState(
            cards = listOf(
                UIState.Card(
                    front = "institute",
                    frontHint = "'ɪn.stɪ.tfuːt",
                    back = "noun [ C ]: an organization whose purpose is to advance the study of a particular subject.",
                    backHint = "The National Institutes of Health fund medical research in many areas.",
                )
            )
        )
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

val UIState.currentCard: UIState.Card
    get() = cards[cardIndex]
val UIState.isCardExpanded: Boolean
    get() = cards[cardIndex].isExpanded