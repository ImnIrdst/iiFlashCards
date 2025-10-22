package com.iid.iiflashcards.data.remote

import android.content.Context
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.services.sheets.v4.Sheets
import com.google.api.services.sheets.v4.model.ValueRange
import com.iid.iiflashcards.R
import com.iid.iiflashcards.data.model.Card
import com.iid.iiflashcards.data.model.RemoteDataSource
import com.iid.iiflashcards.data.sharedpref.AccountPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GoogleSheetsDataSource @Inject constructor(
    val context: Context,
    val accountPreferences: AccountPreferences,
) : RemoteDataSource {

    private val spreadsheetId = "1FYAROmmL-fpISMaOkhBRA2cGG1KA1Y9PPqhptVAT1-4"
    private val range = "Sheet1!A:D"
    private val valueInputOption = "USER_ENTERED"

    private fun getSheetsService(): Sheets {
        val credential = GoogleAccountCredential.usingOAuth2(
            context,
            listOf("https://www.googleapis.com/auth/spreadsheets")
        )
        credential.selectedAccount = accountPreferences.getSavedAccount()

        val transport = NetHttpTransport()
        val jsonFactory = JacksonFactory.getDefaultInstance()
        return Sheets.Builder(transport, jsonFactory, credential)
            .setApplicationName(context.getString(R.string.app_name))
            .build()
    }

    override suspend fun saveCard(card: Card) {
        withContext(Dispatchers.IO) {

            val sheetsService = getSheetsService()

            val values = listOf(
                listOf(
                    card.front,
                    card.frontHint,
                    card.back,
                    card.backHint,
                )
            )
            val body = ValueRange().setValues(values)

            sheetsService.spreadsheets().values()
                .append(spreadsheetId, range, body)
                .setValueInputOption(valueInputOption)
                .execute()
        }
    }
}