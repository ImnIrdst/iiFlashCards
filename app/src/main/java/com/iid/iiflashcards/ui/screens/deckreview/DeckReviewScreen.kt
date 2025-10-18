package com.iid.iiflashcards.ui.screens.deckreview

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.Card
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.iid.iiflashcards.navigation.NavEvent
import com.iid.iiflashcards.ui.ds.Emphasis
import com.iid.iiflashcards.ui.ds.IIScreen
import com.iid.iiflashcards.ui.ds.IIText
import com.iid.iiflashcards.ui.ds.IITextStyle
import com.iid.iiflashcards.ui.theme.IIFlashCardsTheme


@Composable
fun DeckReviewScreen(
    viewModel: DeckReviewViewModel = hiltViewModel(),
    onNavEvent: (NavEvent) -> Unit = {},
) {
    val uiState by viewModel.uiState.collectAsState()
    DeckReviewScreenContent(uiState, viewModel::onEvent, onNavEvent)
}

@Composable
fun DeckReviewScreenContent(
    uiState: UIState,
    onEvent: (Event) -> Unit = {},
    onNavEvent: (NavEvent) -> Unit = {},
) {
    IIScreen(
        topBar = { DeckDetailTopAppBar(onBack = { onNavEvent(NavEvent.PopBackStack) }) },
        floatingActionButton = {
            DeckDetailFab(
                isExpanded = uiState.isCardExpanded,
                onEvent = onEvent,
                onNavEvent = onNavEvent
            )
        },
        floatingActionButtonPosition = FabPosition.Center
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxSize()
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            DeckStats()
            Spacer(modifier = Modifier.height(24.dp))
            Flashcard(uiState, onEvent)
        }
    }
}

@Composable
fun DeckDetailTopAppBar(onBack: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onBack) {
            Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
        }
        Spacer(Modifier.width(8.dp))
        Column(modifier = Modifier.weight(1f)) {
            IIText(
                text = "Common Words",
                style = IITextStyle.HeadlineMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                IIText(text = "55 words", style = IITextStyle.BodySmall, emphasis = Emphasis.Medium)
                Spacer(modifier = Modifier.width(8.dp))
                LinearProgressIndicator(
                    progress = { 0.2f },
                    gapSize = 0.dp,
                    drawStopIndicator = {},
                    modifier = Modifier.padding(2.dp)
                )
            }
        }
        Spacer(modifier = Modifier.width(16.dp))
        IconButton(
            onClick = { /*TODO*/ },
            modifier = Modifier.background(
                color = MaterialTheme.colorScheme.secondaryContainer,
                CircleShape
            )
        ) {
            Icon(
                imageVector = Icons.Rounded.Person,
                contentDescription = "Listen",
            )
        }
    }
}

@Composable
fun DeckStats() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        StatItem(count = 3, label = "Learning", color = Color(0xFF5856D6))
        StatItem(count = 2, label = "Reviewing", color = Color.LightGray)
        StatItem(count = 10, label = "Mastered", color = Color(0xFF34C759))
    }
}

@Composable
fun StatItem(count: Int, label: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .background(color, CircleShape)
            )
            Spacer(modifier = Modifier.width(4.dp))
            IIText(label, style = IITextStyle.BodySmall, emphasis = Emphasis.Medium)
        }
        IIText(
            count.toString(),
            style = IITextStyle.HeadlineMedium,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun Flashcard(state: UIState, onEvent: (Event) -> Unit) {
    val card = state.currentCard ?: return
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
    ) {
        Column {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = MaterialTheme.colorScheme.tertiaryContainer,
                        shape = RoundedCornerShape(24.dp)
                    )
                    .padding(start = 24.dp, end = 24.dp, top = 32.dp, bottom = 24.dp)
            ) {
                Spacer(Modifier.height(128.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column {
                        IIText(
                            text = card.front,
                            style = IITextStyle.HeadlineMedium,
                            fontWeight = FontWeight.Bold
                        )
                        IIText(
                            text = card.frontHint,
                            style = IITextStyle.BodyLarge,
                            emphasis = Emphasis.Medium,
                        )
                    }
                    IconButton(
                        onClick = { /*TODO*/ },
                        modifier = Modifier.background(
                            color = MaterialTheme.colorScheme.tertiary,
                            shape = CircleShape
                        ),
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = MaterialTheme.colorScheme.tertiary,
                            contentColor = MaterialTheme.colorScheme.onTertiary
                        )
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.VolumeUp,
                            contentDescription = "Listen",
                        )
                    }
                }
            }
            AnimatedVisibility(
                visible = state.isCardExpanded,
                enter = expandVertically(
                    expandFrom = Alignment.Top,
                    animationSpec = tween()
                ),
                exit = shrinkVertically(
                    shrinkTowards = Alignment.Top,
                    animationSpec = tween()
                )
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    IIText(
                        text = card.back,
                        style = IITextStyle.BodyLarge
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    IIText(
                        text = card.backHint,
                        style = IITextStyle.BodyMedium,
                        emphasis = Emphasis.Medium
                    )
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        FloatingActionButton(
                            onClick = {
                                onEvent(Event.OnEasy)
                            },
                            shape = RoundedCornerShape(24.dp),
                            modifier = Modifier.size(72.dp),
                            elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 4.dp)
                        ) {
                            IIText("Easy", style = IITextStyle.LabelLarge)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DeckDetailFab(
    isExpanded: Boolean,
    onEvent: (Event) -> Unit,
    onNavEvent: (NavEvent) -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        FloatingActionButton(
            onClick = { /*TODO*/ },
            shape = RoundedCornerShape(16.dp),
            elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 4.dp)
        ) {
            Icon(imageVector = Icons.Default.FavoriteBorder, contentDescription = "difficult")
        }
        FloatingActionButton(
            onClick = { onEvent(Event.OnReveal) },
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier.size(72.dp),
            elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 4.dp)
        ) {
            val icon = if (isExpanded) {
                Icons.Default.VisibilityOff
            } else {
                Icons.Default.Visibility
            }
            Icon(imageVector = icon, contentDescription = "reveal")
        }
        FloatingActionButton(
            onClick = { onNavEvent(NavEvent.AddCard) },
            shape = RoundedCornerShape(16.dp),
            elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 4.dp)
        ) {
            Icon(imageVector = Icons.Default.Add, contentDescription = "Add Card")
        }
    }
}

@PreviewLightDark
@Composable
fun Preview() {
    IIFlashCardsTheme {
        DeckReviewScreenContent(
            uiState = UIState(
                cards = listOf(
                    UIState.Card(
                        front = "institute",
                        frontHint = "'ɪn.stɪ.tfuːt",
                        back = "noun [ C ]: an organization whose purpose is to advance the study of a particular subject.",
                        backHint = "The National Institutes of Health fund medical research in many areas.",
                    )
                )
            )
        )
    }
}
