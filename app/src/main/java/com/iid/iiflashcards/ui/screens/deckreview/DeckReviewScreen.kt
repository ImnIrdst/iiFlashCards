package com.iid.iiflashcards.ui.screens.deckreview

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.iid.iiflashcards.ui.ds.IIScreen

@Composable
fun DeckReviewScreen() {
    IIScreen { modifier ->
        Column(
            modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Hello, Android!",
                style = MaterialTheme.typography.displaySmall,
            )
        }
    }
}
