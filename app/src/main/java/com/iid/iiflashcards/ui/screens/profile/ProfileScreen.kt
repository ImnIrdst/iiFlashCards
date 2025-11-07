package com.iid.iiflashcards.ui.screens.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.iid.iiflashcards.navigation.NavEvent
import com.iid.iiflashcards.ui.ds.IIButton
import com.iid.iiflashcards.ui.ds.IIScreen
import com.iid.iiflashcards.ui.ds.IIText
import com.iid.iiflashcards.ui.ds.IITextStyle
import com.iid.iiflashcards.ui.helper.UserData
import com.iid.iiflashcards.ui.screens.signin.SignInViewModel
import kotlinx.coroutines.launch

@Composable
fun ProfileScreen(signInViewModel: SignInViewModel, onNavEvent: (NavEvent) -> Unit = {}) {
    val lifecycleScope = LocalLifecycleOwner.current.lifecycleScope
    val profileViewModel: ProfileViewModel = hiltViewModel()
    val uiState by profileViewModel.uiState.collectAsState()

    ProfileScreenContent(
        uiState = uiState,
        user = signInViewModel.getUser(),
        doAction = profileViewModel::doAction,
    ) {
        lifecycleScope.launch {
            signInViewModel.signOut()
            onNavEvent(NavEvent.SignIn)
        }
    }
}

@Composable
private fun ProfileScreenContent(
    user: UserData?,
    uiState: ProfileViewModel.UIState,
    doAction: (ProfileViewModel.Action) -> Unit = {},
    onSignOut: () -> Unit = {},
) {
    IIScreen {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            IIText(
                text = "Hello ${user?.username}!",
                style = IITextStyle.DisplaySmall,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.size(64.dp))
            SwitchRow(
                title = "Use cloud TTS",
                isEnabled = uiState.isCloudTTsEnabled
            ) { doAction(ProfileViewModel.Action.ToggleCloudTTS) }
            Spacer(modifier = Modifier.size(8.dp))
            SwitchRow(
                title = "Reveal cards by default",
                isEnabled = uiState.isRevealCardEnabled
            ) { doAction(ProfileViewModel.Action.ToggleRevealCards) }
            Spacer(modifier = Modifier.size(32.dp))
            IIButton(
                text = "Sign out",
                onClick = onSignOut,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun SwitchRow(
    title: String,
    isEnabled: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        IIText(
            text = title,
            style = IITextStyle.TitleLarge,
            fontWeight = FontWeight.Bold,
        )
        Switch(
            checked = isEnabled,
            onCheckedChange = onCheckedChange,
        )
    }
}

@Composable
@PreviewLightDark
private fun Preview() {
    ProfileScreenContent(
        user = UserData(
            userId = "1",
            username = "John Doe",
            profilePictureUrl = null
        ),
        uiState = ProfileViewModel.UIState(isCloudTTsEnabled = true),
    )
}