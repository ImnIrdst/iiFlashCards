package com.iid.iiflashcards.tts

interface TTSHelper {
    suspend fun speak(text: String)

    fun clear()
}
