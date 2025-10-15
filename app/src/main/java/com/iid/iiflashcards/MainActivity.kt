package com.iid.iiflashcards

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.FabPosition
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.iid.iiflashcards.ui.theme.IIFlashCardsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            IIFlashCardsTheme {
                IIFlashCardsApp()
            }
        }
    }
}

@Composable
fun IIFlashCardsApp() {
    HomeScreen()
}

@Composable
fun HomeScreen() {
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
            Spacer(modifier = Modifier.size(16.dp))
            Button(onClick = {}) {
                Text(
                    text = "Hello, Android!",
                    style = MaterialTheme.typography.labelLarge,
                )
            }
        }
    }
}

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

@PreviewLightDark
@Composable
fun GreetingPreview() {
    IIFlashCardsTheme {
        HomeScreen()
    }
}