package com.iid.iiflashcards.ui.screens.signin

import android.widget.Toast
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
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.iid.iiflashcards.navigation.NavEvent
import com.iid.iiflashcards.ui.ds.IIButton
import com.iid.iiflashcards.ui.ds.IIScreen
import kotlinx.coroutines.launch

@Composable
fun SignInScreen(
    viewModel: SignInViewModel,
    onNavEvent: (NavEvent) -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val lifecycleScope = LocalLifecycleOwner.current.lifecycleScope
    val currentContext = LocalContext.current


    LaunchedEffect(key1 = Unit) {
        if (viewModel.getUser() != null) {
            onNavEvent(NavEvent.Home)
        }
    }

    LaunchedEffect(key1 = state.isSignInSuccessful) {
        if (state.isSignInSuccessful) {
            Toast.makeText(
                currentContext,
                "Sign in successful",
                Toast.LENGTH_LONG
            ).show()

            onNavEvent(NavEvent.Home)
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
                    viewModel.signIn()
                }
            }
        }
    }
}