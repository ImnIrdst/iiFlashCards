package com.iid.iiflashcards

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.iid.iiflashcards.navigation.AppNavigation
import com.iid.iiflashcards.ui.screens.home.HomeScreen
import com.iid.iiflashcards.ui.theme.IIFlashCardsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppNavigation()
        }
    }
}

@PreviewLightDark
@Composable
fun Preview() {
    IIFlashCardsTheme {
        HomeScreen()
    }
}
