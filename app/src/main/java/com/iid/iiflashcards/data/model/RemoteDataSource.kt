package com.iid.iiflashcards.data.model

interface RemoteDataSource {
    suspend fun saveCard(card: Card)
}

class MockRemoteDataSource : RemoteDataSource {
    override suspend fun saveCard(card: Card) {
        // Simulate a network call
        kotlinx.coroutines.delay(1000)
    }
}
