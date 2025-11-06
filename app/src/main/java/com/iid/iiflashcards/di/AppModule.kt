package com.iid.iiflashcards.di

import android.content.Context
import androidx.room.Room
import com.iid.iiflashcards.data.local.AppDatabase
import com.iid.iiflashcards.data.sharedpref.SettingsPreferences
import com.iid.iiflashcards.tts.AndroidTTS
import com.iid.iiflashcards.tts.GoogleCloudTTS
import com.iid.iiflashcards.tts.TTSHelper
import com.iid.iiflashcards.tts.TTSHelperImpl
import com.iid.iiflashcards.util.DateHelper
import com.iid.iiflashcards.util.DateHelperImpl
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
        context,
        AppDatabase::class.java,
        "flashcards-db",
    ).fallbackToDestructiveMigration(false).build()

    @Provides
    fun provideCardDao(database: AppDatabase) = database.cardDao()

    @Provides
    @Singleton
    fun provideTTs(
        localTTS: AndroidTTS,
        remoteTTS: GoogleCloudTTS,
        settingsPreferences: SettingsPreferences,
    ): TTSHelper = TTSHelperImpl(localTTS, remoteTTS, settingsPreferences)

    @Provides
    fun provideDateHelper(): DateHelper = DateHelperImpl
}
