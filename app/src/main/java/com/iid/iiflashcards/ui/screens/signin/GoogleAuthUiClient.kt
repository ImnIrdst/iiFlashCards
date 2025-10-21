package com.iid.iiflashcards.ui.screens.signin

import android.content.Context
import android.content.Intent
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.Scope
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.firebase.Firebase
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import com.iid.iiflashcards.R
import com.iid.iiflashcards.util.logDebugMessage
import com.iid.iiflashcards.util.logGenericError
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.cancellation.CancellationException

class GoogleAuthUiClient(
    private val context: Context,
    private val credentialManager: CredentialManager = CredentialManager.create(context)
) {
    private val auth = Firebase.auth

    fun getSignInIntent(): Intent {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(R.string.default_web_client_id))
            .requestEmail()
            .requestScopes(Scope("https://www.googleapis.com/auth/spreadsheets"))
            .build()

        return GoogleSignIn.getClient(context, gso).signInIntent
    }

    suspend fun handleSignInResult(data: Intent?) {
        try {
            logDebugMessage("handleSignInResult $data extras: ${data?.extras}")
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            val account = task.getResult(ApiException::class.java)!!

            firebaseAuthWithGoogle(account.idToken!!)

            initializeSheetsApi(account)

        } catch (e: ApiException) {
            logGenericError(e = e)
        }
    }

    private fun initializeSheetsApi(account: GoogleSignInAccount) {
        // Use the account to create the credential
        val credential = GoogleAccountCredential.usingOAuth2(
            context,
            listOf("https://www.googleapis.com/auth/spreadsheets")
        )
        credential.selectedAccount = account.account!! // Set the active account
        logDebugMessage("Sheets API initialized! ${account.account}")
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