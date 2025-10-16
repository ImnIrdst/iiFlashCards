package com.iid.iiflashcards.ui.ds

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.iid.iiflashcards.ui.theme.IIFlashCardsTheme

@Composable
fun IIScreen(
    modifier: Modifier = Modifier,
    topBar: @Composable () -> Unit = {},
    floatingActionButton: @Composable () -> Unit = {},
    floatingActionButtonPosition: FabPosition = FabPosition.Center,
    content: @Composable () -> Unit = {},
) {
    IIFlashCardsTheme {
        Scaffold(
            modifier = modifier,
            topBar = {
                Box(Modifier.windowInsetsPadding(WindowInsets.statusBars)) {
                    topBar()
                }
            },
            bottomBar = { },
            snackbarHost = { },
            floatingActionButton = floatingActionButton,
            floatingActionButtonPosition = floatingActionButtonPosition,
        ) { contentPadding ->
            Box(modifier = Modifier.padding(paddingValues = contentPadding)) {
                content()
            }
        }
    }
}