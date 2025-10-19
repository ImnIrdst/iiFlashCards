package com.iid.iiflashcards.ui.auth

import android.content.Context
import android.content.Intent
import android.content.IntentSender
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.SignInClient
import io.jsonwebtoken.Jwts
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.tasks.await

class GoogleAuthUiClient(
    private val context: Context,
    private val oneTapClient: SignInClient
) {
    private val R_string_web_client_id =
        "701117525566-6pp0sti98ba5fet1ulo8r75bmus23io9.apps.googleusercontent.com"

    suspend fun signIn(): IntentSender? {
        val result = try {
            oneTapClient.beginSignIn(
                BeginSignInRequest.builder()
                    .setGoogleIdTokenRequestOptions(
                        BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                            .setSupported(true)
                            .setServerClientId(R_string_web_client_id)
                            .setFilterByAuthorizedAccounts(false)
                            .build()
                    )
                    .setAutoSelectEnabled(true)
                    .build()
            ).await()
        } catch (e: Exception) {
            e.printStackTrace()
            if (e is CancellationException) throw e
            null
        }
        return result?.pendingIntent?.intentSender
    }

    suspend fun signInWithIntent(intent: Intent): SignInResult {
        val credential = oneTapClient.getSignInCredentialFromIntent(intent)
        val googleIdToken = credential.googleIdToken
        val googleId = googleIdToken?.let {
            val webClientId = R_string_web_client_id
            if (webClientId == null) {
                return SignInResult(
                    data = null,
                    errorMessage = "Web client ID not found"
                )
            }
            try {
//                val claims = Jwts.parser().setSigningKey(webClientId.toByteArray()).parseClaimsJws(it)
                val claims = Jwts.parser()
                    .setSigningKey(webClientId.toByteArray())
                    .build()
                    .parseClaimsJws(it)
                claims.body.subject
            } catch (e: Exception) {
                e.printStackTrace()
                if (e is CancellationException) throw e
                null
            }
        }

        return SignInResult(
            data = googleId?.let {
                UserData(
                    userId = it,
                    username = credential.id,
                    profilePictureUrl = credential.profilePictureUri?.toString()
                )
            },
            errorMessage = null
        )
    }

    suspend fun signOut() {
        try {
            oneTapClient.signOut().await()
        } catch (e: Exception) {
            e.printStackTrace()
            if (e is CancellationException) throw e
        }
    }

    fun getSignedInUser(): UserData? {
        println("IMN HERE 1")
        val credential = oneTapClient.getSignInCredentialFromIntent(Intent())
        println("IMN HERE 2")
        val googleIdToken = credential.googleIdToken
        val googleId = googleIdToken?.let {
            val webClientId = R_string_web_client_id
            if (webClientId == null) {
                return null
            }
            try {
//                val claims =
//                    Jwts.parser().setSigningKey(webClientId.toByteArray()).parseClaimsJws(it)

                val claims = Jwts.parser()
                    .setSigningKey(webClientId.toByteArray())
                    .build()
                    .parseClaimsJws(it)
                claims.body.subject
            } catch (e: Exception) {
                e.printStackTrace()
                if (e is CancellationException) throw e
                null
            }
        }

        return googleId?.let {
            UserData(
                userId = it,
                username = credential.id,
                profilePictureUrl = credential.profilePictureUri?.toString()
            )
        }
    }
}