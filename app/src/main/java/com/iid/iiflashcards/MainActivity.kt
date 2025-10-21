package com.iid.iiflashcards

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.lifecycle.lifecycleScope
import com.iid.iiflashcards.navigation.AppNavigation
import com.iid.iiflashcards.ui.screens.home.HomeScreen
import com.iid.iiflashcards.ui.screens.signin.GoogleAuthUiClient
import com.iid.iiflashcards.ui.screens.signin.SignInViewModel
import com.iid.iiflashcards.ui.theme.IIFlashCardsTheme
import com.iid.iiflashcards.util.logGenericError
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val signInViewModel: SignInViewModel by viewModels()

    private val googleAuthUiClient: GoogleAuthUiClient by lazy {
        GoogleAuthUiClient(context = this)
    }

    private val signInLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            lifecycleScope.launch {
                googleAuthUiClient.handleSignInResult(result.data)
                signInViewModel.setUser(googleAuthUiClient.getSignedInUser())
            }
        } else {
            logGenericError(message = "Sign in error $result")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setUpSignInViewModel()

        enableEdgeToEdge()
        setContent {
            IIFlashCardsTheme {
                AppNavigation(signInViewModel)
            }
        }
    }

    private fun setUpSignInViewModel() = with(signInViewModel) {
        setUser(googleAuthUiClient.getSignedInUser())
        event.observe(this@MainActivity) { event ->
            lifecycleScope.launch {
                when (event) {
                    is SignInViewModel.Event.SignIn -> {
                        val intent = googleAuthUiClient.getSignInIntent()
                        signInLauncher.launch(intent)
                    }

                    is SignInViewModel.Event.SignOut -> {
                        googleAuthUiClient.signOut()
                        resetState()
                    }
                }
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
