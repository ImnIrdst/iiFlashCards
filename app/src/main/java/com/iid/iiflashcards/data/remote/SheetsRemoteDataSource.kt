package com.iid.iiflashcards.data.remote

import com.google.api.client.auth.oauth2.Credential
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.services.sheets.v4.Sheets
import com.google.api.services.sheets.v4.model.ValueRange
import java.io.IOException

class SheetsRemoteDataSource(
    private val credential: Credential
) {

    private val transport = GoogleNetHttpTransport.newTrustedTransport()
    private val jsonFactory = JacksonFactory.getDefaultInstance()
    private val sheets: Sheets = Sheets.Builder(transport, jsonFactory, credential)
        .setApplicationName("iiFlashCards")
        .build()

    // TODO: Replace with your spreadsheet ID
    private val spreadsheetId = "YOUR_SPREADSHEET_ID"
    
    // TODO: Replace with your desired data range
    private val range = "Sheet1!A1:D"

    @Throws(IOException::class)
    fun getFlashcards(): List<List<Any>>? {
        val response: ValueRange = sheets.spreadsheets().values()
            .get(spreadsheetId, range)
            .execute()
        return response.getValues()
    }
    
    @Throws(IOException::class)
    fun updateFlashcards(values: List<List<Any>>): Int? {
        val body = ValueRange().setValues(values)
        val result = sheets.spreadsheets().values()
            .update(spreadsheetId, range, body)
            .setValueInputOption("RAW")
            .execute()
        return result.updatedCells
    }
}