package com.iid.iiflashcards.ui.helper

import android.accounts.Account
import android.content.Context
import android.content.Intent
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.Scope
import com.google.api.services.sheets.v4.SheetsScopes
import com.google.firebase.Firebase
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import com.iid.iiflashcards.R
import com.iid.iiflashcards.data.sharedpref.AccountPreferences
import com.iid.iiflashcards.util.logDebugMessage
import com.iid.iiflashcards.util.logGenericError
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.cancellation.CancellationException

class GoogleAuthHelper(
    private val context: Context,
    private val accountPrefs: AccountPreferences = AccountPreferences(context.applicationContext),
    private val credentialManager: CredentialManager = CredentialManager.Companion.create(context),
) {
    private val auth = Firebase.auth

    fun getSignInIntent(): Intent {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(R.string.default_web_client_id))
            .requestEmail()
            .requestScopes(Scope(SheetsScopes.SPREADSHEETS))
            .build()



        return GoogleSignIn.getClient(context, gso).signInIntent
    }

    suspend fun handleSignInResult(data: Intent?): Account? {
        try {
            logDebugMessage("handleSignInResult $data extras: ${data?.extras}")
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            val account = task.getResult(ApiException::class.java)!!

            firebaseAuthWithGoogle(account.idToken!!)

            accountPrefs.saveAccount(account.account)
            return account.account
        } catch (e: ApiException) {
            logGenericError(e = e)
        }
        return null
    }

    private suspend fun firebaseAuthWithGoogle(idToken: String): UserData? {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        try {
            auth.signInWithCredential(credential).await()
        } catch (e: Exception) {
            e.printStackTrace()
            if (e is CancellationException) throw e
        }
        return getSignedInUser()
    }

    suspend fun signOut() {
        try {
            auth.signOut()
            accountPrefs.clear()
            val clearRequest = ClearCredentialStateRequest()
            credentialManager.clearCredentialState(clearRequest)
        } catch (e: Exception) {
            e.printStackTrace()
            if (e is CancellationException) throw e
        }
    }

    fun getSignedInUser(): UserData? = auth.currentUser?.run {
        UserData(
            userId = uid,
            username = displayName,
            profilePictureUrl = photoUrl?.toString()
        )
    }
}

data class UserData(
    val userId: String,
    val username: String?,
    val profilePictureUrl: String?
)