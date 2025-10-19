package com.iid.iiflashcards

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.auth.api.identity.Identity
import com.iid.iiflashcards.navigation.AppNavigation
import com.iid.iiflashcards.ui.auth.GoogleAuthUiClient
import com.iid.iiflashcards.ui.auth.SignInScreen
import com.iid.iiflashcards.ui.auth.SignInViewModel
import com.iid.iiflashcards.ui.screens.home.HomeScreen
import com.iid.iiflashcards.ui.theme.IIFlashCardsTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val googleAuthUiClient by lazy {
        GoogleAuthUiClient(
            context = applicationContext,
            oneTapClient = Identity.getSignInClient(applicationContext)
        )
    }

    private val viewModel: SignInViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            IIFlashCardsTheme {
                val state by viewModel.state.collectAsState()

//                 Check for a signed-in user when the app starts
                LaunchedEffect(key1 = Unit) {
                    val signedInUser = googleAuthUiClient.getSignedInUser()
                    if (signedInUser != null) {
                        viewModel.setSignedIn()
                    } else {
                        viewModel.setSignedOut()
                    }
                }

                val launcher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.StartIntentSenderForResult(),
                    onResult = { result ->
                        println("TODO imn result.resultCode ${result.resultCode}")
                        if (result.resultCode == RESULT_OK) {
                            lifecycleScope.launch {
                                val signInResult = googleAuthUiClient.signInWithIntent(
                                    intent = result.data ?: return@launch
                                )
                                viewModel.onSignInResult(signInResult)
                            }
                        }
                    }
                )

                // Show a toast message upon successful sign-in
                LaunchedEffect(key1 = state.isSignInSuccessful) {
                    if (state.isSignInSuccessful) {
                        Toast.makeText(
                            applicationContext,
                            "Sign in successful",
                            Toast.LENGTH_LONG
                        ).show()
                        viewModel.resetState()
                    }
                }

                // Show UI based on the ViewModel's state, not a direct call
                when {
//                    state.isSignInSuccessful == false -> {
//                        // Initial loading state
//                        Box(
//                            modifier = Modifier.fillMaxSize(),
//                            contentAlignment = Alignment.Center
//                        ) {
//                            CircularProgressIndicator()
//                        }
//                    }
                    state.isSignInSuccessful -> {
                        // If sign-in is successful, show the main app
                        AppNavigation()
                    }

                    else -> {
                        // Otherwise, show the sign-in screen
                        SignInScreen(
                            state = state,
                            onSignInClick = {
                                lifecycleScope.launch {
                                    val signInIntentSender = googleAuthUiClient.signIn()
                                    println("TODO imn ${signInIntentSender}")
                                    launcher.launch(
                                        IntentSenderRequest.Builder(
                                            signInIntentSender ?: return@launch
                                        ).build()
                                    )
                                }
                            }
                        )
                    }
                }
//                LaunchedEffect(key1 = state.isSignInSuccessful) {
//                    if (state.isSignInSuccessful) {
//                        Toast.makeText(
//                            applicationContext,
//                            "Sign in successful",
//                            Toast.LENGTH_LONG
//                        ).show()
//
//                        viewModel.resetState()
//                    }
//                }
//
//                if (googleAuthUiClient.getSignedInUser() != null) {
//                    AppNavigation()
//                } else {
//                    SignInScreen(
//                        state = state,
//                        onSignInClick = {
//                            lifecycleScope.launch {
//                                val signInIntentSender = googleAuthUiClient.signIn()
//                                launcher.launch(
//                                    IntentSenderRequest.Builder(
//                                        signInIntentSender ?: return@launch
//                                    ).build()
//                                )
//                            }
//                        }
//                    )
//                }
            }
        }
    }
}

@PreviewLightDark
@Composable
fun Preview() {
    IIFlashCardsTheme {
        HomeScreen()
    }
}
