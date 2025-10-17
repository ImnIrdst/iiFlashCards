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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.iid.iiflashcards.navigation.NavEvent
import com.iid.iiflashcards.ui.ds.IIScreen
import com.iid.iiflashcards.ui.ds.IIText
import com.iid.iiflashcards.ui.ds.IITextStyle
import com.iid.iiflashcards.ui.theme.IIFlashCardsTheme

@Composable
fun AddCardScreen(
    viewModel: AddCardViewModel = viewModel(),
    onNavEvent: (NavEvent) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    IIScreen(
        topBar = {
            AddCardTopAppBar(onNavEvent)
        }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            OutlinedTextField(
                value = uiState.front,
                onValueChange = { viewModel.onEvent(AddCardEvent.OnFrontChange(it)) },
                label = { Text("Front") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = uiState.frontHint,
                onValueChange = { viewModel.onEvent(AddCardEvent.OnFrontHintChange(it)) },
                label = { Text("Front Hint (optional)") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = uiState.back,
                onValueChange = { viewModel.onEvent(AddCardEvent.OnBackChange(it)) },
                label = { Text("Back") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = uiState.backHint,
                onValueChange = { viewModel.onEvent(AddCardEvent.OnBackHintChange(it)) },
                label = { Text("Back Hint (optional)") },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun AddCardTopAppBar(onNavEvent: (NavEvent) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = { onNavEvent(NavEvent.PopBackStack) }) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
        }
        IIText(
            text = "Add New Card",
            style = IITextStyle.HeadlineSmall,
            modifier = Modifier.weight(1f)
        )
        IconButton(onClick = { onNavEvent(NavEvent.PopBackStack) }) {
            Icon(Icons.Default.Done, contentDescription = "Save")
        }
    }
}

@PreviewLightDark
@Composable
private fun PreviewAddCardScreen() {
    IIFlashCardsTheme {
        AddCardScreen()
    }
}
