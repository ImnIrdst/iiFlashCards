package com.iid.iiflashcards.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.iid.iiflashcards.data.local.converter.DateConverter
import com.iid.iiflashcards.data.model.CardEntity

@Database(entities = [CardEntity::class], version = 3, exportSchema = true)
@TypeConverters(DateConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun cardDao(): CardDao
}