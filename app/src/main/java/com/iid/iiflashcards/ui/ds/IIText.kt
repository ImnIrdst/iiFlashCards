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
fun IIText(
    text: String,
    style: IITextStyle,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.onBackground,
    emphasis: Emphasis = Emphasis.High,
    fontWeight: FontWeight = FontWeight.Normal,
    textAlign: TextAlign = TextAlign.Center
) {
    val style = when (style) {
        IITextStyle.DisplayLarge -> MaterialTheme.typography.displayLarge
        IITextStyle.HeadlineLarge -> MaterialTheme.typography.headlineLarge
        IITextStyle.HeadlineMedium -> MaterialTheme.typography.headlineMedium
        IITextStyle.HeadlineSmall -> MaterialTheme.typography.headlineSmall
        IITextStyle.TitleLarge -> MaterialTheme.typography.titleLarge
        IITextStyle.BodyLarge -> MaterialTheme.typography.bodyLarge
        IITextStyle.BodyMedium -> MaterialTheme.typography.bodyMedium
        IITextStyle.BodySmall -> MaterialTheme.typography.bodySmall
        IITextStyle.LabelLarge -> MaterialTheme.typography.labelLarge
    }

    Text(
        text = text,
        style = style.copy(fontWeight = fontWeight),
        color = color.copy(alpha = emphasis.alpha),
        modifier = modifier,
        textAlign = textAlign,
    )
}


sealed class Emphasis(
    val alpha: Float
) {
    data object High : Emphasis(0.87f)
    data object Medium : Emphasis(0.6f)
    data object Disabled : Emphasis(0.38f)
}

sealed class IITextStyle {
    data object DisplayLarge : IITextStyle()
    data object HeadlineLarge : IITextStyle()
    data object TitleLarge : IITextStyle()
    data object BodyLarge : IITextStyle()
    data object BodyMedium : IITextStyle()
    data object BodySmall : IITextStyle()
    data object HeadlineMedium : IITextStyle()
    data object LabelLarge : IITextStyle()
    data object HeadlineSmall : IITextStyle()
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
                PreviewGroup(IITextStyle.DisplayLarge, false)
                PreviewGroup(IITextStyle.HeadlineLarge, false)
                PreviewGroup(IITextStyle.HeadlineMedium, false)
                PreviewGroup(IITextStyle.HeadlineSmall, false)
                PreviewGroup(IITextStyle.TitleLarge)
                PreviewGroup(IITextStyle.BodyLarge)
                PreviewGroup(IITextStyle.BodyMedium)
                PreviewGroup(IITextStyle.BodySmall)
                PreviewGroup(IITextStyle.LabelLarge)
            }
        }
    }
}

@Composable
private fun PreviewGroup(style: IITextStyle, hideOtherStyles: Boolean = true) {
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