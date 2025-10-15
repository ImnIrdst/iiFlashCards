package com.iid.iiflashcards.ui.ds

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable


@Composable
fun IIButton(
    text: String,
    style: IIButtonStyle = IIButtonStyle.Primary,
    onClick: () -> Unit
) {

    val buttonColors = when (style) {
        is IIButtonStyle.Primary -> ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
        )
    }

    Button(
        onClick = onClick,
        colors = buttonColors,
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge,
        )
    }
}

sealed class IIButtonStyle {
    object Primary : IIButtonStyle()
}
