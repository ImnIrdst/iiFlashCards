package com.iid.iiflashcards.data.repository

import com.iid.iiflashcards.data.local.CardDao
import com.iid.iiflashcards.data.model.Card
import com.iid.iiflashcards.data.model.RemoteDataSource
import kotlinx.coroutines.flow.Flow

interface CardRepository {
    suspend fun saveCard(card: Card)
    fun getAllCards(): Flow<List<Card>>
}

class CardRepositoryImpl(
    private val cardDao: CardDao,
    private val remoteDataSource: RemoteDataSource
) : CardRepository {

    override suspend fun saveCard(card: Card) {
        cardDao.insertCard(card)
        remoteDataSource.saveCard(card)
    }

    override fun getAllCards(): Flow<List<Card>> = cardDao.getAllCards()
}
