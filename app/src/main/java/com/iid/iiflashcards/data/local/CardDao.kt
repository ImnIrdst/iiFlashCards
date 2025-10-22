package com.iid.iiflashcards.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.iid.iiflashcards.data.model.Card
import kotlinx.coroutines.flow.Flow

@Dao
interface CardDao {
    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertCard(card: Card): Long

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertCards(card: List<Card>)

    @Query("SELECT * FROM cards")
    fun getAllCardsFlow(): Flow<List<Card>>

    @Query("SELECT * FROM cards")
    suspend fun getAllCards(): List<Card>

    @Query("DELETE FROM cards")
    suspend fun deleteAllCards()
}