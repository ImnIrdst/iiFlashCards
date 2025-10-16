package com.iid.iiflashcards.ui.ds

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.iid.iiflashcards.ui.theme.IIFlashCardsTheme

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

sealed class Emphasis(
    val alpha: Float
) {
    object High : Emphasis(0.87f)
    object Medium : Emphasis(0.6f)
    object Disabled : Emphasis(0.38f)
}

sealed class Style {
    object HeadlineLarge : Style()
    object TitleLarge : Style()
    object BodyLarge : Style()
    object BodyMedium : Style()
    object BodySmall : Style()
    object HeadlineMedium : Style()
    object LabelLarge : Style()
}


@Composable
fun IIText(
    text: String,
    style: Style,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.onBackground,
    emphasis: Emphasis = Emphasis.High,
    fontWeight: FontWeight = FontWeight.Normal
) {
    val style = when (style) {
        Style.HeadlineLarge -> MaterialTheme.typography.headlineLarge
        Style.TitleLarge -> MaterialTheme.typography.titleLarge
        Style.BodyLarge -> MaterialTheme.typography.bodyLarge
        Style.BodyMedium -> MaterialTheme.typography.bodyMedium
        Style.BodySmall -> MaterialTheme.typography.bodySmall
        Style.HeadlineMedium -> MaterialTheme.typography.headlineMedium
        Style.LabelLarge -> MaterialTheme.typography.labelLarge
    }

    Text(
        text = text,
        style = style.copy(fontWeight = fontWeight),
        color = color.copy(alpha = emphasis.alpha),
        modifier = modifier
    )
}


@Preview(heightDp = 1024, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun Preview() {
    IIFlashCardsTheme {
        Surface {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                PreviewGroup(Style.HeadlineLarge, false)
                PreviewGroup(Style.HeadlineMedium, false)
                PreviewGroup(Style.TitleLarge)
                PreviewGroup(Style.BodyLarge)
                PreviewGroup(Style.BodyMedium)
                PreviewGroup(Style.BodySmall)
                PreviewGroup(Style.LabelLarge)
            }
        }
    }
}

@Composable
private fun PreviewGroup(style: Style, hideOtherStyles: Boolean = true) {
    Column {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            IIText(
                text = "${style::class.simpleName}",
                style = style,
            )
            if (hideOtherStyles) {
                IIText(
                    text = "${style::class.simpleName}",
                    style = style,
                    emphasis = Emphasis.Medium,
                )
                IIText(
                    text = "${style::class.simpleName}",
                    style = style,
                    emphasis = Emphasis.Disabled,
                )
            }

        }
    }
}