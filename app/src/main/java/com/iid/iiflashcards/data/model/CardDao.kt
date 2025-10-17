package com.iid.iiflashcards.data.model

import androidx.room.Dao
import androidx.room.Insert

@Dao
interface CardDao {
    @Insert
    suspend fun insertCard(card: Card)
}
