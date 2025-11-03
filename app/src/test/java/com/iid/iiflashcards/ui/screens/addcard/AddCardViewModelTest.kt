package com.iid.iiflashcards.ui.screens.addcard

import com.iid.iiflashcards.data.model.CardEntity
import com.iid.iiflashcards.data.repository.CardRepository
import com.iid.iiflashcards.util.DateHelperImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import java.util.Date

@ExperimentalCoroutinesApi
class AddCardViewModelTest {

    private lateinit var viewModel: AddCardViewModel
    private lateinit var cardRepository: CardRepository
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        cardRepository = mock()
        viewModel = AddCardViewModel(cardRepository)
        DateHelperImpl.currentTestDate = Date(1624137600000)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        DateHelperImpl.currentTestDate = null
    }

    @Test
    fun `OnFrontChange updates front in uiState`() {
        val testString = "new front"
        viewModel.doAction(Action.OnFrontChange(testString))
        assertEquals(testString, viewModel.uiState.value.front)
    }

    @Test
    fun `OnFrontHintChange updates frontHint in uiState`() {
        val testString = "new front hint"
        viewModel.doAction(Action.OnFrontHintChange(testString))
        assertEquals(testString, viewModel.uiState.value.frontHint)
    }

    @Test
    fun `OnBackChange updates back in uiState`() {
        val testString = "new back"
        viewModel.doAction(Action.OnBackChange(testString))
        assertEquals(testString, viewModel.uiState.value.back)
    }

    @Test
    fun `OnBackHintChange updates backHint in uiState`() {
        val testString = "new back hint"
        viewModel.doAction(Action.OnBackHintChange(testString))
        assertEquals(testString, viewModel.uiState.value.backHint)
    }

    @Test
    fun `OnSave calls saveCard on repository`() = runTest {
        val front = "front"
        val back = "back"
        val frontHint = "front hint"
        val backHint = "back hint"

        viewModel.doAction(Action.OnFrontChange(front))
        viewModel.doAction(Action.OnBackChange(back))
        viewModel.doAction(Action.OnFrontHintChange(frontHint))
        viewModel.doAction(Action.OnBackHintChange(backHint))

        viewModel.doAction(Action.OnSave)
        testDispatcher.scheduler.advanceUntilIdle()

        val expectedCard = CardEntity(
            front = front,
            frontHint = frontHint,
            back = back,
            backHint = backHint
        )

        verify(cardRepository).saveCard(expectedCard)
    }
}
