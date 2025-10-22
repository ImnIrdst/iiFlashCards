package com.iid.iiflashcards.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.iid.iiflashcards.data.model.Card

@Database(entities = [Card::class], version = 1, exportSchema = true)
abstract class AppDatabase : RoomDatabase() {
    abstract fun cardDao(): CardDao
}