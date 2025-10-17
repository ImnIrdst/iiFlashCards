package com.iid.iiflashcards.data.repository

import com.iid.iiflashcards.data.model.Card
import com.iid.iiflashcards.data.model.CardDao
import com.iid.iiflashcards.data.model.RemoteDataSource

interface CardRepository {
    suspend fun saveCard(card: Card)
}

class CardRepositoryImpl(
    private val cardDao: CardDao,
    private val remoteDataSource: RemoteDataSource
) : CardRepository {

    override suspend fun saveCard(card: Card) {
        cardDao.insertCard(card)
        remoteDataSource.saveCard(card)
    }
}
