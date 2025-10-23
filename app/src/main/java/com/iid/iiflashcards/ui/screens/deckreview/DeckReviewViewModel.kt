package com.iid.iiflashcards.ui.screens.deckreview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iid.iiflashcards.data.model.CardEntity
import com.iid.iiflashcards.data.repository.CardRepository
import com.iid.iiflashcards.util.ReviewDateHelper
import com.iid.iiflashcards.util.ReviewDateHelper.Repetition.Again
import com.iid.iiflashcards.util.ReviewDateHelper.Repetition.Easy
import com.iid.iiflashcards.util.ReviewDateHelper.Repetition.Good
import com.iid.iiflashcards.util.ReviewDateHelper.Repetition.Hard
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DeckReviewViewModel @Inject constructor(
    private val cardRepository: CardRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(UIState())
    val uiState: StateFlow<UIState> = _uiState

    init {
        cardRepository.getAllCardsFlow().onEach { cards ->
            _uiState.value = _uiState.value.copy(
                cards = cards.map {
                    UIState.Card(
                        front = it.front,
                        frontHint = it.frontHint ?: "...",
                        back = it.back,
                        backHint = it.backHint ?: "...",
                        cardEntity = it,
                    )
                }.sortedWith(UIState.comparator)
            )
        }.launchIn(viewModelScope)
        onRefresh()
    }

    fun onAction(action: Action) {
        when (action) {
            is Action.OnReveal -> onReveal()
            is Action.OnRefresh -> onRefresh()
            is Action.OnEasy -> onUpdateCardDate(Easy)
            is Action.OnAgain -> onUpdateCardDate(Again)
            is Action.OnGood -> onUpdateCardDate(Good)
            is Action.OnHard -> onUpdateCardDate(Hard)
        }
    }

    private fun onUpdateCardDate(repetition: ReviewDateHelper.Repetition) = viewModelScope.launch {
        val currentCard = uiState.value.currentCard?.cardEntity ?: return@launch

        val newCard = currentCard.copy(
            reviewDate = ReviewDateHelper.updateDate(currentCard.reviewDate, repetition)
        )

        cardRepository.updateCard(newCard)
        updateCount()
    }

    private fun updateCount() {
        _uiState.value =
            _uiState.value.copy(reviewedCardsCount = _uiState.value.reviewedCardsCount + 1)
    }

    private fun onReveal() {
        _uiState.value = _uiState.value.toggleCardReveal()
    }

    private fun onRefresh() = viewModelScope.launch {
        _uiState.value = _uiState.value.copy(isRefreshing = true)
        cardRepository.refreshCards()
        _uiState.value = _uiState.value.copy(isRefreshing = false)
    }
}

sealed class Action {
    data object OnAgain : Action()
    data object OnHard : Action()
    data object OnGood : Action()
    data object OnEasy : Action()

    data object OnReveal : Action()
    data object OnRefresh : Action()
}

data class UIState(
    val reviewedCardsCount: Int = 0,
    val isRefreshing: Boolean = false,
    val cards: List<Card> = emptyList(),
) {
    data class Card(
        val front: String,
        val frontHint: String,
        val back: String,
        val backHint: String,
        val cardEntity: CardEntity? = null,
        val isExpanded: Boolean = true,
    )

    companion object {
        val comparator: Comparator<Card>
            get() = compareBy<Card> { it.cardEntity?.reviewDate }
                .thenBy { it.cardEntity?.id }
    }
}

fun UIState.toggleCardReveal() = copy(
    cards = cards.mapIndexed { index, card ->
        if (index == 0) {
            card.copy(isExpanded = !card.isExpanded)
        } else {
            card
        }
    }
)

val UIState.currentCard: UIState.Card?
    get() = cards.firstOrNull()
val UIState.isCardExpanded: Boolean
    get() = cards.firstOrNull()?.isExpanded ?: true

val UIState.progress: Float
    get() = if (cards.isEmpty()) 0f else (reviewedCardsCount / cards.size.toFloat())
