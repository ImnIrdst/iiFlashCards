package com.iid.iiflashcards.di

import android.content.Context
import androidx.room.Room
import com.iid.iiflashcards.data.model.AppDatabase
import com.iid.iiflashcards.data.model.CardDao
import com.iid.iiflashcards.data.model.MockRemoteDataSource
import com.iid.iiflashcards.data.model.RemoteDataSource
import com.iid.iiflashcards.data.repository.CardRepository
import com.iid.iiflashcards.data.repository.CardRepositoryImpl
import com.iid.iiflashcards.ui.screens.signin.GoogleAuthUiClient
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
    fun provideRemoteDataSource(): RemoteDataSource {
        return MockRemoteDataSource()
    }

    @Provides
    @Singleton
    fun provideCardRepository(
        cardDao: CardDao,
        remoteDataSource: RemoteDataSource
    ): CardRepository {
        return CardRepositoryImpl(cardDao, remoteDataSource)
    }

    @Provides
    @Singleton
    fun provideGoogleAuthUiClient(@ApplicationContext context: Context): GoogleAuthUiClient {
        return GoogleAuthUiClient(
            context = context,
        )
    }
}
