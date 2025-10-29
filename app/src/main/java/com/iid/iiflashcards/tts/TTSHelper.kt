package com.iid.iiflashcards.tts

import com.iid.iiflashcards.data.sharedpref.SettingsPreferences

interface TTSHelper {
    suspend fun speak(text: String)

    fun clear()
}

class TTSHelperImpl(
    private val localTTS: TTSHelper,
    private val remoteTTS: TTSHelper,
    private val settingsPreferences: SettingsPreferences,
) : TTSHelper {
    override suspend fun speak(text: String) {
        if (settingsPreferences.isCloudTSSAgentEnabled()) {
            remoteTTS.speak(text)
        } else {
            localTTS.speak(text)
        }
    }

    override fun clear() {
        localTTS.clear()
        remoteTTS.clear()
    }
}
