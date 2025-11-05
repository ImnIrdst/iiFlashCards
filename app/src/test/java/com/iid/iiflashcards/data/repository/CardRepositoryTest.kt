package com.iid.iiflashcards.data.repository

import com.iid.iiflashcards.data.local.CardDao
import com.iid.iiflashcards.data.model.CardEntity
import com.iid.iiflashcards.data.remote.GoogleSheetsDataSource
import com.iid.iiflashcards.util.DateHelper
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.util.Date

@ExperimentalCoroutinesApi
class CardRepositoryTest {

    private lateinit var cardDao: CardDao
    private lateinit var remoteDataSource: GoogleSheetsDataSource
    private lateinit var dateHelper: DateHelper
    private lateinit var cardRepository: CardRepository

    private val testTomorrowDate = Date(1624137600000)
    private val testCard = CardEntity(id = 1, front = "front", back = "back")

    private val testUpdatedCard = CardEntity(
        id = 1,
        front = "front",
        back = "back",
        reviewDate = testTomorrowDate,
    )

    @Before
    fun setUp() {
        cardDao = mock()
        remoteDataSource = mock()
        dateHelper = mock()
        cardRepository = CardRepository(cardDao, remoteDataSource, dateHelper)
        whenever(dateHelper.getTomorrowDate()).thenReturn(testTomorrowDate)
    }

    @Test
    fun `saveCard should call insertCard on dao and saveCards on remoteDataSource`() = runTest {
        whenever(cardDao.getAllCards()).thenReturn(listOf(testCard))

        cardRepository.saveCard(testCard)

        verify(cardDao).insertCard(testUpdatedCard)
        verify(remoteDataSource).saveCards(listOf(testUpdatedCard))
    }

    @Test
    fun `updateCard should call updateCard on dao and saveCards on remoteDataSource`() = runTest {
        whenever(cardDao.getAllCards()).thenReturn(listOf(testCard))

        cardRepository.updateCard(testCard)

        verify(cardDao).updateCard(testUpdatedCard)
        verify(remoteDataSource).saveCards(listOf(testUpdatedCard))
    }

    @Test
    fun `updateCard with null card should do nothing`() = runTest {
        cardRepository.updateCard(null)

        verify(cardDao, never()).updateCard(testCard)
        verify(remoteDataSource, never()).saveCards(emptyList())
    }

    @Test
    fun `getAllCards should call getAllCards on dao`() = runTest {
        whenever(cardDao.getAllCards())
            .thenReturn(listOf(testCard))

        val result = cardRepository.getAllCards()

        assertEquals(listOf(testUpdatedCard), result)
        verify(cardDao).getAllCards()
    }

    @Test
    fun `refreshCards should fetch from remote and update local`() = runTest {
        whenever(remoteDataSource.getAllCards())
            .thenReturn(listOf(testCard))

        cardRepository.refreshCards()

        verify(remoteDataSource).getAllCards()
        verify(cardDao).insertCards(listOf(testUpdatedCard))
    }

    @Test
    fun `getAllCardsFlow should call getAllCardsFlow on dao`() = runTest {
        whenever(cardDao.getAllCardsFlow())
            .thenReturn(flowOf(listOf(testCard)))

        val result = cardRepository.getAllCardsFlow()
        result.collect {
            assertEquals(listOf(testCard), it)
        }

        @Suppress("UnusedFlow")
        verify(cardDao).getAllCardsFlow()
    }
}
