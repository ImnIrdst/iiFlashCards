package com.iid.iiflashcards.data.repository

import com.iid.iiflashcards.data.local.CardDao
import com.iid.iiflashcards.data.model.Card
import com.iid.iiflashcards.data.remote.GoogleSheetsDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CardRepository @Inject constructor(
    private val cardDao: CardDao,
    private val remoteDataSource: GoogleSheetsDataSource
) {

    suspend fun saveCard(card: Card) {
        cardDao.insertCard(card)
        remoteDataSource.saveCards(getAllCards())
    }

    suspend fun getAllCards() = cardDao.getAllCards()

    suspend fun refreshCards() {
        val allCards = remoteDataSource.getAllCards()

        cardDao.deleteAllCards()
        cardDao.insertCards(allCards)
    }

    fun getAllCardsFlow(): Flow<List<Card>> = cardDao.getAllCardsFlow()
}
