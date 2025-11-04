package com.iid.iiflashcards.data.repository

import com.iid.iiflashcards.data.local.CardDao
import com.iid.iiflashcards.data.model.CardEntity
import com.iid.iiflashcards.data.remote.GoogleSheetsDataSource
import com.iid.iiflashcards.util.DateHelper
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CardRepository @Inject constructor(
    private val cardDao: CardDao,
    private val remoteDataSource: GoogleSheetsDataSource,
    private val dateHelper: DateHelper,
) {

    suspend fun saveCard(card: CardEntity) {
        cardDao.insertCard(card)
        remoteDataSource.saveCards(getAllCards())
    }

    suspend fun updateCard(card: CardEntity?) {
        card ?: return

        cardDao.updateCard(card.fixDate())
        remoteDataSource.saveCards(getAllCards().fixDates())
    }

    suspend fun getAllCards() = cardDao.getAllCards().fixDates()

    suspend fun refreshCards() {
        val allCards = remoteDataSource.getAllCards().fixDates()

        cardDao.deleteAllCards()
        cardDao.insertCards(allCards)
    }

    fun getAllCardsFlow(): Flow<List<CardEntity>> = cardDao.getAllCardsFlow()

    private fun CardEntity.fixDate() = if (reviewDate == null) {
        copy(reviewDate = dateHelper.getTomorrowDate())
    } else {
        this
    }

    private fun List<CardEntity>.fixDates() = map { it.fixDate() }
}
