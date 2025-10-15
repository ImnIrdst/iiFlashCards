package com.iid.iiflashcards.ui.ds

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun IIScreen(modifier: Modifier = Modifier, content: @Composable (Modifier) -> Unit) {
    Scaffold(
        modifier = modifier,
        topBar = { },
        bottomBar = { },
        snackbarHost = { },
        floatingActionButton = { },
        floatingActionButtonPosition = FabPosition.Center,
    ) { contentPadding ->
        content(Modifier.padding(paddingValues = contentPadding))
    }
}