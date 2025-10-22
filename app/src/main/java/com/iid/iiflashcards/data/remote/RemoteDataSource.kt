package com.iid.iiflashcards.data.remote

import com.iid.iiflashcards.data.model.Card

interface RemoteDataSource {
    suspend fun saveCard(card: Card)
}