package com.iid.iiflashcards.ui.screens.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.iid.iiflashcards.navigation.NavEvent
import com.iid.iiflashcards.ui.ds.IIButton
import com.iid.iiflashcards.ui.ds.IIScreen
import com.iid.iiflashcards.ui.ds.IIText
import com.iid.iiflashcards.ui.ds.IITextStyle
import com.iid.iiflashcards.ui.screens.signin.SignInViewModel
import kotlinx.coroutines.launch

@Composable
fun ProfileScreen(viewModel: SignInViewModel, onNavEvent: (NavEvent) -> Unit = {}) {
    val lifecycleScope = LocalLifecycleOwner.current.lifecycleScope

    IIScreen {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            IIText(
                text = "Hello ${viewModel.getUser()?.username}!",
                style = IITextStyle.DisplayLarge,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.size(16.dp))
            IIButton(text = "Sign out", onClick = {
                lifecycleScope.launch {
                    viewModel.signOut()
                    onNavEvent(NavEvent.SignIn)
                }
            })
        }
    }
}