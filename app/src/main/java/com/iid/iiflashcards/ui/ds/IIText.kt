package com.iid.iiflashcards.ui.ds

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun IIText(text: String, style: IITextStyle) {
    val style = when (style) {
        IITextStyle.DisplayLarge -> MaterialTheme.typography.displayLarge
    }

    Text(
        text = text,
        style = style,
    )
}

sealed class IITextStyle {
    object DisplayLarge : IITextStyle()
}