package com.iid.iiflashcards.ui.screens.signin

import android.app.Activity.RESULT_OK
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.iid.iiflashcards.navigation.NavEvent
import com.iid.iiflashcards.ui.ds.IIButton
import com.iid.iiflashcards.ui.ds.IIScreen
import kotlinx.coroutines.launch

@Composable
fun SignInScreen(
    onNavEvent: (NavEvent) -> Unit,
) {
    val viewModel = hiltViewModel<SignInViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val lifecycleScope = LocalLifecycleOwner.current.lifecycleScope
    val currentContext = LocalContext.current


    LaunchedEffect(key1 = Unit) {
        println("TODO imn googleAuthUiClient.getSignedInUser() ${viewModel.googleAuthUiClient.getSignedInUser()}")
        if (viewModel.googleAuthUiClient.getSignedInUser() != null) {
            onNavEvent(NavEvent.Home)
        }
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult(),
        onResult = { result ->
            println("TODO imn result $result")
            if (result.resultCode == RESULT_OK) {
                lifecycleScope.launch {
                    val signInResult = viewModel.googleAuthUiClient.signInWithIntent(
                        intent = result.data ?: return@launch
                    )
                    viewModel.onSignInResult(signInResult)
                }
            }
        }
    )

    LaunchedEffect(key1 = state.isSignInSuccessful) {
        if (state.isSignInSuccessful) {
            Toast.makeText(
                currentContext,
                "Sign in successful",
                Toast.LENGTH_LONG
            ).show()

            onNavEvent(NavEvent.Home)
            viewModel.resetState()
        }
    }

    LaunchedEffect(key1 = state.signInError) {
        state.signInError?.let { error ->
            Toast.makeText(
                currentContext,
                error,
                Toast.LENGTH_LONG
            ).show()
        }
    }

    IIScreen {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            IIButton(text = "sign in") {
                lifecycleScope.launch {
                    val signInIntentSender = viewModel.googleAuthUiClient.signIn()
                    launcher.launch(
                        IntentSenderRequest.Builder(
                            signInIntentSender ?: return@launch
                        ).build()
                    )
                }
            }
        }
    }

}