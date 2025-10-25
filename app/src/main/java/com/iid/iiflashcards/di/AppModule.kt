package com.iid.iiflashcards.di

import android.content.Context
import androidx.room.Room
import com.iid.iiflashcards.data.local.AppDatabase
import com.iid.iiflashcards.tts.GoogleCloudTTS
import com.iid.iiflashcards.tts.TTSHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context) = Room.databaseBuilder(
        context, AppDatabase::class.java, "flashcards-db",
    ).build()

    @Provides
    fun provideCardDao(database: AppDatabase) = database.cardDao()

    @Provides
    @Singleton
    fun provideTTs(googleCloudTTS: GoogleCloudTTS): TTSHelper = googleCloudTTS
}
