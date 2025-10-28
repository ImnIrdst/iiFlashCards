package com.iid.iiflashcards.data.sharedpref

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class SettingsPreferences @Inject constructor(
    @ApplicationContext context: Context,
) {

    private companion object {
        const val PREFS_NAME = "settings_prefs"
        const val KEY_TSS_AGENT = "tss_agent"
    }


    private val settingsPrefs: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun setTssAgent(isCloud: Boolean) {
        settingsPrefs.edit().apply {
            putBoolean(KEY_TSS_AGENT, isCloud)
            apply()
        }
    }

    fun isCloudTSSAgentEnabled() = settingsPrefs.getBoolean(KEY_TSS_AGENT, false)

    @Suppress("unused")
    fun clear() {
        settingsPrefs.edit { clear() }
    }
}