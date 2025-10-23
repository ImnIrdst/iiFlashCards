package com.iid.iiflashcards.data.remote

import android.content.Context
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.services.sheets.v4.Sheets
import com.google.api.services.sheets.v4.SheetsScopes
import com.google.api.services.sheets.v4.model.ValueRange
import com.iid.iiflashcards.R
import com.iid.iiflashcards.data.local.converter.dateToLong
import com.iid.iiflashcards.data.local.converter.longToDate
import com.iid.iiflashcards.data.model.CardEntity
import com.iid.iiflashcards.data.sharedpref.AccountPreferences
import com.iid.iiflashcards.util.getTomorrowDate
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
    private val range = "$sheetName!A:F"
    private val valueInputOption = "USER_ENTERED"

    private val sheetsService: Sheets by lazy {
        val credential = GoogleAccountCredential.usingOAuth2(
            context,
            listOf(SheetsScopes.SPREADSHEETS)
        )
        credential.selectedAccount = accountPreferences.getSavedAccount()

        val transport = NetHttpTransport()
        val jsonFactory = JacksonFactory.getDefaultInstance()
        Sheets.Builder(transport, jsonFactory, credential)
            .setApplicationName(context.getString(R.string.app_name))
            .build()
    }

    suspend fun saveCards(cards: List<CardEntity>) = withContext(Dispatchers.IO) {
        val sortedList = cards.sortedBy { it.id }

        val values = sortedList.map { card ->
            listOf(
                card.id,
                card.front,
                card.frontHint,
                card.back,
                card.backHint,
                dateToLong(card.reviewDate).toString(),
            )
        }
        val body = ValueRange().setValues(values)
        sheetsService.spreadsheets().values()
            .update(spreadsheetId, range, body)
            .setValueInputOption(valueInputOption)
            .execute()

        return@withContext true
    }

    suspend fun getAllCards() = withContext(Dispatchers.IO) {
        val response = sheetsService.spreadsheets().values()
            .get(spreadsheetId, range)
            .execute()

        response.getValues()?.mapNotNull { row ->
            val id = row.getOrNull(0).toString().toLongOrNull()
            if (row.size >= 5 && id != null) {
                CardEntity(
                    id = id,
                    front = row[1].toString(),
                    frontHint = row[2].toString(),
                    back = row[3].toString(),
                    backHint = row[4].toString(),
                    reviewDate = longToDate(
                        value = row.getOrNull(5)?.toString()?.toLong()
                    ) ?: getTomorrowDate()
                )
            } else {
                null
            }
        } ?: emptyList()
    }
}
