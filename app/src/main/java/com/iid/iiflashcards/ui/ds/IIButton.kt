package com.iid.iiflashcards.ui.ds

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.iid.iiflashcards.ui.theme.IIFlashCardsTheme


@Composable
fun IIButton(
    text: String,
    subText: String? = null,
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
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            IIText(
                text = text,
                style = IITextStyle.LabelLarge,
            )
            if (subText != null) {
                IIText(
                    text = subText,
                    style = IITextStyle.LabelLarge,
                    emphasis = Emphasis.Medium
                )
            }
        }
    }
}

sealed class IIButtonStyle {
    object Primary : IIButtonStyle()
}

@PreviewLightDark
@Composable
private fun Preview() {
    IIFlashCardsTheme {
        IIScreen {
            Column(modifier = Modifier.padding(16.dp)) {
                IIButton(text = "Text", subText = "subtext") {}
            }
        }
    }
}
