package com.iid.iiflashcards.ui.ds

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.iid.iiflashcards.ui.theme.IIFlashCardsTheme
import com.iid.iiflashcards.ui.theme.extendedColorScheme


@Composable
fun IIButton(
    text: String,
    modifier: Modifier = Modifier,
    subText: String? = null,
    style: IIButtonStyle = IIButtonStyle.Primary,
    onClick: () -> Unit,
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
        shape = RoundedCornerShape(16.dp),
        modifier = modifier,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(8.dp)
        ) {
            IIText(
                text = text,
                style = IITextStyle.LabelLarge,
                fontWeight = FontWeight.Bold
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

@Composable
fun IIButtonOutlined(
    text: String,
    subText: String? = null,
    style: IIButtonOutLinedStyle = IIButtonOutLinedStyle.Success,
    onClick: () -> Unit
) {
    IIFlashCardsTheme(darkTheme = true) {

        val buttonColors = when (style) {
            IIButtonOutLinedStyle.Success -> ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.extendedColorScheme.success.color,
                contentColor = MaterialTheme.extendedColorScheme.success.colorContainer,
            )

            IIButtonOutLinedStyle.Info -> ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.extendedColorScheme.info.color,
                contentColor = MaterialTheme.extendedColorScheme.info.colorContainer,
            )

            IIButtonOutLinedStyle.Warning -> ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.extendedColorScheme.warning.color,
                contentColor = MaterialTheme.extendedColorScheme.warning.colorContainer,
            )

            IIButtonOutLinedStyle.Error -> ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.errorContainer,
                contentColor = MaterialTheme.colorScheme.error,
            )
        }

        Button(
            onClick = onClick,
            colors = buttonColors,
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(
                width = 2.dp,
                color = buttonColors.contentColor
            )
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                IIText(
                    text = text,
                    style = IITextStyle.LabelLarge,
                    fontWeight = FontWeight.Bold,
                    color = buttonColors.contentColor
                )
                if (subText != null) {
                    IIText(
                        text = subText,
                        style = IITextStyle.LabelLarge,
                        emphasis = Emphasis.Medium,
                        color = buttonColors.contentColor
                    )
                }
            }
        }
    }

}


sealed class IIButtonStyle {
    object Primary : IIButtonStyle()
}

sealed class IIButtonOutLinedStyle {
    object Success : IIButtonOutLinedStyle()
    object Info : IIButtonOutLinedStyle()
    object Warning : IIButtonOutLinedStyle()
    object Error : IIButtonOutLinedStyle()
}

@PreviewLightDark
@Composable
private fun Preview() {
    IIFlashCardsTheme {
        IIScreen {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                IIButton(text = "Text") {}
                IIButton(text = "Text", modifier = Modifier.fillMaxWidth()) {}
                IIButton(text = "Text", subText = "subtext") {}
                IIButtonOutlined(
                    text = "Text",
                    subText = "subtext",
                    style = IIButtonOutLinedStyle.Success
                ) {}
                IIButtonOutlined(
                    text = "Text",
                    subText = "subtext",
                    style = IIButtonOutLinedStyle.Info
                ) {}
                IIButtonOutlined(
                    text = "Text",
                    subText = "subtext",
                    style = IIButtonOutLinedStyle.Warning
                ) {}
                IIButtonOutlined(
                    text = "Text",
                    subText = "subtext",
                    style = IIButtonOutLinedStyle.Error
                ) {}
            }
        }
    }
}
