package com.iid.iiflashcards.data.model

interface RemoteDataSource {
    suspend fun saveCard(card: Card)
}
