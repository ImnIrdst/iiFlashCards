package com.iid.iiflashcards.di

import android.content.Context
import androidx.room.Room
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.services.sheets.v4.SheetsScopes
import com.iid.iiflashcards.data.local.AppDatabase
import com.iid.iiflashcards.data.local.CardDao
import com.iid.iiflashcards.data.remote.GoogleSheetsDataSource
import com.iid.iiflashcards.data.remote.RemoteDataSource
import com.iid.iiflashcards.data.repository.CardRepository
import com.iid.iiflashcards.data.repository.CardRepositoryImpl
import com.iid.iiflashcards.data.sharedpref.AccountPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.util.Collections
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {


    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "flashcards-db"
        ).build()
    }

    @Provides
    fun provideCardDao(database: AppDatabase): CardDao {
        return database.cardDao()
    }

    @Provides
    @Singleton
    fun provideGoogleAccountCredential(@ApplicationContext context: Context): GoogleAccountCredential {
        return GoogleAccountCredential.usingOAuth2(
            context,
            Collections.singleton(SheetsScopes.SPREADSHEETS)
        )
    }

    @Provides
    @Singleton
    fun provideRemoteDataSource(
        @ApplicationContext context: Context,
        accountPreferences: AccountPreferences,
    ): RemoteDataSource {
        return GoogleSheetsDataSource(context, accountPreferences)
    }

    @Provides
    @Singleton
    fun provideCardRepository(
        cardDao: CardDao,
        remoteDataSource: RemoteDataSource
    ): CardRepository {
        return CardRepositoryImpl(cardDao, remoteDataSource)
    }
}
