package com.iid.iiflashcards.data.sharedpref

import android.accounts.Account
import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class AccountPreferences @Inject constructor(
    @ApplicationContext context: Context,
) {

    private companion object {
        const val PREFS_NAME = "auth_prefs"
        const val KEY_ACCOUNT_NAME = "account_name"
        const val KEY_ACCOUNT_TYPE = "account_type"
    }


    private val authPrefs: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun saveAccount(account: Account?) {
        if (account == null) return

        authPrefs.edit().apply {
            putString(KEY_ACCOUNT_NAME, account.name)
            putString(KEY_ACCOUNT_TYPE, account.type)
            apply()
        }
    }

    fun getSavedAccount(): Account? {
        val name = authPrefs.getString(KEY_ACCOUNT_NAME, null) ?: return null
        val type = authPrefs.getString(KEY_ACCOUNT_TYPE, null) ?: return null

        return Account(name, type)
    }

    fun clear() {
        authPrefs.edit { clear() }
    }
}