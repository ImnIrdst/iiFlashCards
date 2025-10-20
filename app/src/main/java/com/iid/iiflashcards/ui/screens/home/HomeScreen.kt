package com.iid.iiflashcards.ui.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.iid.iiflashcards.navigation.NavEvent
import com.iid.iiflashcards.ui.ds.IIButton
import com.iid.iiflashcards.ui.ds.IIScreen
import com.iid.iiflashcards.ui.ds.IIText
import com.iid.iiflashcards.ui.ds.IITextStyle

@Composable
fun HomeScreen(onNavEvent: (NavEvent) -> Unit = {}) {
    IIScreen {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            IIText(text = "Hello there!", style = IITextStyle.DisplayLarge)
            Spacer(modifier = Modifier.size(16.dp))
            IIButton(text = "Start Learning", onClick = {
                onNavEvent(NavEvent.DeckReview)
            })
        }
    }
}
