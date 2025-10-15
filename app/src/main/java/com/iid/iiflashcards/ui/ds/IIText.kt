package com.iid.iiflashcards.ui.ds

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.style.TextAlign

@Composable
fun IIText(text: String, style: IITextStyle) {
    val style = when (style) {
        IITextStyle.DisplayLarge -> MaterialTheme.typography.displayLarge
    }

    Text(
        text = text,
        style = style,
        textAlign = TextAlign.Center
    )
}

sealed class IITextStyle {
    object DisplayLarge : IITextStyle()
}