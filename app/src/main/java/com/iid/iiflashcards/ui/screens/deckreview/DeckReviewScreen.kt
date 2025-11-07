package com.iid.iiflashcards.ui.screens.deckreview

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Style
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.iid.iiflashcards.navigation.NavEvent
import com.iid.iiflashcards.ui.ds.Emphasis
import com.iid.iiflashcards.ui.ds.IIButtonOutLinedStyle.Error
import com.iid.iiflashcards.ui.ds.IIButtonOutLinedStyle.Info
import com.iid.iiflashcards.ui.ds.IIButtonOutLinedStyle.Success
import com.iid.iiflashcards.ui.ds.IIButtonOutLinedStyle.Warning
import com.iid.iiflashcards.ui.ds.IIButtonOutlined
import com.iid.iiflashcards.ui.ds.IIScreen
import com.iid.iiflashcards.ui.ds.IIText
import com.iid.iiflashcards.ui.ds.IITextStyle
import com.iid.iiflashcards.ui.theme.IIFlashCardsTheme
import com.iid.iiflashcards.util.showNotImplementedToast
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.roundToInt


@Composable
fun DeckReviewScreen(
    viewModel: DeckReviewViewModel = hiltViewModel(),
    onNavEvent: (NavEvent) -> Unit = {},
) {
    val uiState by viewModel.uiState.collectAsState()
    DeckReviewScreenContent(uiState, viewModel::onAction, onNavEvent)
}

@Composable
fun DeckReviewScreenContent(
    uiState: UIState,
    onAction: (Action) -> Unit = {},
    onNavEvent: (NavEvent) -> Unit = {},
) {
    IIScreen(
        topBar = { DeckDetailTopAppBar(uiState, onAction = onAction, onNavEvent = onNavEvent) },
        floatingActionButton = {
            DeckDetailFab(
                isExpanded = uiState.isCardExpanded,
                onAction = onAction,
                onNavEvent = onNavEvent,
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
            Flashcard(uiState, onAction)
            Spacer(modifier = Modifier.size(128.dp))
        }
    }
}

@Composable
fun DeckDetailTopAppBar(
    uiState: UIState,
    onAction: (Action) -> Unit,
    onNavEvent: (NavEvent) -> Unit,
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = { onNavEvent(NavEvent.PopBackStack) }) {
            Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
        }
        Spacer(Modifier.width(8.dp))
        Column(modifier = Modifier.weight(1f)) {
            IIText(
                text = "Common Words",
                style = IITextStyle.HeadlineSmall,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                IIText(
                    text = "${uiState.cards.size} words",
                    style = IITextStyle.BodySmall,
                    emphasis = Emphasis.Medium
                )
                Spacer(modifier = Modifier.width(8.dp))
                LinearProgressIndicator(
                    progress = { uiState.progress },
                    gapSize = 0.dp,
                    drawStopIndicator = {},
                    modifier = Modifier.padding(2.dp)
                )
            }
        }
        Spacer(modifier = Modifier.width(16.dp))
        if (uiState.isRefreshing) {
            CircularProgressIndicator(
                modifier = Modifier
                    .padding(4.dp)
                    .size(32.dp)
            )
        } else {
            IconButton(
                onClick = { onAction(Action.OnRefresh) }, modifier = Modifier
                    .background(
                        color = MaterialTheme.colorScheme.background, CircleShape
                    )
                    .size(40.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Listen",
                )
            }
        }

        Spacer(modifier = Modifier.width(8.dp))
        IconButton(
            onClick = { onNavEvent(NavEvent.Profile) }, modifier = Modifier.background(
                color = MaterialTheme.colorScheme.secondaryContainer, CircleShape
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
        modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround
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
            count.toString(), style = IITextStyle.HeadlineMedium, fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun Flashcard(state: UIState, onAction: (Action) -> Unit) {
    val card = state.currentCard ?: return

    val coroutineScope = rememberCoroutineScope()
    val startOffset = previewOffset ?: LocalWindowInfo.current.containerSize.width.dp
    val offsetX = remember { Animatable(startOffset.value) }

    LaunchedEffect(card.front) {
        delay(750)
        offsetX.snapTo(startOffset.value)
        offsetX.animateTo(
            targetValue = 0f,
            animationSpec = tween(durationMillis = 300)
        )
    }

    fun animateAndPerformAction(action: Action) {
        coroutineScope.launch {
            offsetX.animateTo(
                targetValue = -startOffset.value,
                animationSpec = tween(durationMillis = 300)
            )
            onAction(action)
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onAction(Action.OnReveal) }
            .offset { IntOffset(offsetX.value.roundToInt(), 0) },
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
                Spacer(Modifier.height(64.dp))
                Row(
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(Modifier.weight(1f)) {
                        IIText(
                            text = card.front,
                            style = IITextStyle.HeadlineMedium,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Start,
                        )
                        Spacer(Modifier.size(8.dp))
                        IIText(
                            text = card.frontHint,
                            style = IITextStyle.BodyMedium,
                            emphasis = Emphasis.Medium,
                            textAlign = TextAlign.Start,
                            modifier = Modifier.padding(end = 8.dp)
                        )
                    }
                    IconButton(
                        onClick = { onAction(Action.OnSpeak) },
                        modifier = Modifier.background(
                            color = MaterialTheme.colorScheme.tertiary, shape = CircleShape
                        ), colors = IconButtonDefaults.iconButtonColors(
                            containerColor = MaterialTheme.colorScheme.tertiary,
                            contentColor = MaterialTheme.colorScheme.onTertiary,
                        )
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.VolumeUp,
                            tint = MaterialTheme.colorScheme.onTertiary,
                            contentDescription = "Listen",
                        )
                    }
                }
            }
            AnimatedVisibility(
                visible = state.isCardExpanded, enter = expandVertically(
                    expandFrom = Alignment.Top, animationSpec = tween()
                ), exit = shrinkVertically(
                    shrinkTowards = Alignment.Top, animationSpec = tween()
                )
            ) {
                Column {
                    Column(modifier = Modifier.padding(24.dp)) {
                        IIText(
                            text = card.back, style = IITextStyle.TitleMedium,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Start,
                        )
                        Spacer(modifier = Modifier.size(8.dp))
                        IIText(
                            text = card.backHint,
                            style = IITextStyle.BodyMedium,
                            emphasis = Emphasis.Medium,
                            textAlign = TextAlign.Start,
                        )
                    }
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(
                            space = 1.dp,
                            alignment = Alignment.CenterHorizontally,
                        ),
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp)
                    ) {
                        IIButtonOutlined(text = "Again", subText = "1m", style = Error) {
                            animateAndPerformAction(Action.OnAgain)
                        }
                        IIButtonOutlined(text = "Hard", subText = "10m", style = Warning) {
                            animateAndPerformAction(Action.OnHard)
                        }
                        IIButtonOutlined(text = "Good", subText = "1d", style = Info) {
                            animateAndPerformAction(Action.OnGood)
                        }
                        IIButtonOutlined(text = "Easy", subText = "4d", style = Success) {
                            animateAndPerformAction(Action.OnEasy)
                        }
                    }
                }

            }
        }
    }
}

@Composable
fun DeckDetailFab(
    isExpanded: Boolean, onAction: (Action) -> Unit, onNavEvent: (NavEvent) -> Unit
) {
    val context = LocalContext.current
    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        FloatingActionButton(
            onClick = { context.showNotImplementedToast() },
            shape = RoundedCornerShape(16.dp),
            elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 4.dp)
        ) {
            Icon(imageVector = Icons.Default.Style, contentDescription = "difficult")
        }
        FloatingActionButton(
            onClick = { onAction(Action.OnReveal) },
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

var previewOffset: Dp? = null

@PreviewLightDark
@Composable
fun Preview() {
    previewOffset = 0.dp
    IIFlashCardsTheme {
        DeckReviewScreenContent(
            uiState = UIState(
                cards = listOf(
                    UIState.Card(
                        front = "Aflevering",
                        frontHint = "De nieuwste aflevering van mijn favoriete serie is net uitgekomen.",
                        back = "Episode, Installment, Part, Segment",
                        backHint = "The latest episode of my favorite show just came out.",
                    )
                )
            )
        )
    }
}
