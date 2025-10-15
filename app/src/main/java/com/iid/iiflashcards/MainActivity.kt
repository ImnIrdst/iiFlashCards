package com.iid.iiflashcards

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
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
fun HomeScreen(modifier: Modifier = Modifier) {
    Surface(Modifier.fillMaxSize()) {
        Column(Modifier.systemBarsPadding()) {
            Text(
                text = "Hello, Android!",
                style = MaterialTheme.typography.displaySmall,
                modifier = modifier
            )
        }
    }
}

@PreviewLightDark
@Composable
fun GreetingPreview() {
    IIFlashCardsTheme {
        HomeScreen()
    }
}