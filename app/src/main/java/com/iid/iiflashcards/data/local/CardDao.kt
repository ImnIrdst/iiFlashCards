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
    suspend fun insertCard(card: Card)

    @Query("SELECT * FROM cards")
    fun getAllCards(): Flow<List<Card>>
}