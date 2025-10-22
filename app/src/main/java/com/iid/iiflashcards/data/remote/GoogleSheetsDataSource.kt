package com.iid.iiflashcards.data.remote

import android.content.Context
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.services.sheets.v4.Sheets
import com.google.api.services.sheets.v4.SheetsScopes
import com.google.api.services.sheets.v4.model.ClearValuesRequest
import com.google.api.services.sheets.v4.model.ValueRange
import com.iid.iiflashcards.R
import com.iid.iiflashcards.data.model.Card
import com.iid.iiflashcards.data.sharedpref.AccountPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

@Suppress("UsePropertyAccessSyntax")
class GoogleSheetsDataSource @Inject constructor(
    @param:ApplicationContext
    val context: Context,
    val accountPreferences: AccountPreferences,
) {

    private val spreadsheetId = "1FYAROmmL-fpISMaOkhBRA2cGG1KA1Y9PPqhptVAT1-4"

    private val sheetName = "Sheet1"
    private val range = "Sheet1!A:E"
    private val valueInputOption = "USER_ENTERED"

    private fun getSheetsService(): Sheets {
        val credential = GoogleAccountCredential.usingOAuth2(
            context,
            listOf(SheetsScopes.SPREADSHEETS)
        )
        credential.selectedAccount = accountPreferences.getSavedAccount()

        val transport = NetHttpTransport()
        val jsonFactory = JacksonFactory.getDefaultInstance()
        return Sheets.Builder(transport, jsonFactory, credential)
            .setApplicationName(context.getString(R.string.app_name))
            .build()
    }

    /**
     * This function clears all data from the specified sheet.
     */
    suspend fun clearAllData() = withContext(Dispatchers.IO) {
        val sheetsService = getSheetsService()
        sheetsService.spreadsheets().values()
            .clear(spreadsheetId, sheetName, ClearValuesRequest())
            .execute()
    }


    suspend fun saveCards(card: List<Card>) = withContext(Dispatchers.IO) {
        clearAllData()
        
        val sortedList = card.sortedBy { it.id }

        val sheetsService = getSheetsService()

        val values = sortedList.map { card ->
            listOf(
                card.id,
                card.front,
                card.frontHint,
                card.back,
                card.backHint,
            )
        }
        val body = ValueRange().setValues(values)

        sheetsService.spreadsheets().values()
            .append(spreadsheetId, range, body)
            .setValueInputOption(valueInputOption)
            .execute()

        return@withContext true
    }

    suspend fun getAllCards() = withContext(Dispatchers.IO) {
        val sheetsService = getSheetsService()

        val response = sheetsService.spreadsheets().values()
            .get(spreadsheetId, range)
            .execute()

        response.getValues()?.mapNotNull { row ->
            if (row.size >= 5) {
                Card(
                    id = row[0].toString().toLong(),
                    front = row[1].toString(),
                    frontHint = row[2].toString(),
                    back = row[3].toString(),
                    backHint = row[4].toString()
                )
            } else {
                null
            }
        } ?: emptyList()
    }
}
