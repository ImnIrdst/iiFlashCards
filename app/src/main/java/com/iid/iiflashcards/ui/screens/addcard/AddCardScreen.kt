package com.iid.iiflashcards.ui.screens.addcard

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.iid.iiflashcards.navigation.NavEvent
import com.iid.iiflashcards.ui.ds.IIScreen
import com.iid.iiflashcards.ui.ds.IIText
import com.iid.iiflashcards.ui.ds.IITextStyle
import com.iid.iiflashcards.ui.theme.IIFlashCardsTheme

@Composable
fun AddCardScreen(
    viewModel: AddCardViewModel = hiltViewModel(),
    onNavEvent: (NavEvent) -> Unit = {},
) {
    val uiState by viewModel.uiState.collectAsState()

    AddCardScreenContent(
        uiState,
        onAction = viewModel::doAction,
        onNavEvent = onNavEvent
    )
}

@Composable
fun AddCardScreenContent(
    uiState: AddCardUiState,
    onAction: (Action) -> Unit = {},
    onNavEvent: (NavEvent) -> Unit = {}
) {
    IIScreen(
        topBar = {
            AddCardTopAppBar(onNavEvent, onAction)
        }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            OutlinedTextField(
                value = uiState.front,
                onValueChange = { onAction(Action.OnFrontChange(it)) },
                label = { Text("Front") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = uiState.frontHint,
                onValueChange = { onAction(Action.OnFrontHintChange(it)) },
                label = { Text("Front Hint (optional)") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = uiState.back,
                onValueChange = { onAction(Action.OnBackChange(it)) },
                label = { Text("Back") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = uiState.backHint,
                onValueChange = { onAction(Action.OnBackHintChange(it)) },
                label = { Text("Back Hint (optional)") },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun AddCardTopAppBar(onNavEvent: (NavEvent) -> Unit, onAction: (Action) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = { onNavEvent(NavEvent.PopBackStack) }) {
            Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
        }
        IIText(
            text = "Add New Card",
            style = IITextStyle.HeadlineSmall,
            modifier = Modifier.weight(1f)
        )
        IconButton(
            onClick = {
                onAction(Action.OnSave)
                onNavEvent(NavEvent.PopBackStack)
            }) {
            Icon(imageVector = Icons.Default.Done, contentDescription = "Save")
        }
    }
}

@PreviewLightDark
@Composable
private fun PreviewAddCardScreen() {
    IIFlashCardsTheme {
        AddCardScreenContent(uiState = AddCardUiState())
    }
}
