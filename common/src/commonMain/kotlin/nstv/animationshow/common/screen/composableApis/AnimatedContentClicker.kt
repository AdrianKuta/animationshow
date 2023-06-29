package nstv.animationshow.common.screen.composableApis

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.with
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import nstv.animationshow.common.design.Grid
import nstv.animationshow.common.design.TileColor
import nstv.animationshow.common.design.components.CheckBoxLabel
import nstv.animationshow.common.design.components.DropDownWithArrows
import nstv.animationshow.common.screen.base.enterTransitions
import nstv.animationshow.common.screen.base.exitTransitions
import nstv.animationshow.common.screen.base.exitTransitionsOpposite


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AnimatedContentClickerScreen(
    modifier: Modifier = Modifier,
) {
    var enterTransitionIndex by remember { mutableStateOf(enterTransitions.keys.indexOf("slideIn")) }
    var exitTransitionIndex by remember { mutableStateOf(exitTransitions.keys.indexOf("slideOut")) }
    var backgroundColor by remember { mutableStateOf(TileColor.list.first()) }
    var tapCounter by remember { mutableStateOf(0) }
    var observeColorInsteadOfTap by remember { mutableStateOf(false) }
    var oppositeDirections by remember { mutableStateOf(true) }

    Column(modifier = modifier, verticalArrangement = Arrangement.Bottom) {
        DropDownWithArrows(
            modifier = modifier.fillMaxWidth(),
            options = enterTransitions.keys.toList(),
            onSelectionChanged = { enterTransitionIndex = it },
            selectedIndex = enterTransitionIndex,
            label = "Enter:"
        )

        DropDownWithArrows(
            modifier = modifier.fillMaxWidth(),
            options = exitTransitions.keys.toList(),
            onSelectionChanged = { exitTransitionIndex = it },
            selectedIndex = exitTransitionIndex,
            label = "Exit:"
        )

        CheckBoxLabel(
            text = "Observe color instead of tap",
            checked = observeColorInsteadOfTap,
            onCheckedChange = { observeColorInsteadOfTap = it }
        )

        CheckBoxLabel(
            text = "Opposite directions",
            checked = oppositeDirections,
            onCheckedChange = { oppositeDirections = it }
        )

        Box(modifier = Modifier.fillMaxSize().background(color = TileColor.Magenta.copy(alpha = 0.2f))) {

            val onTap: () -> Unit = {
                if (observeColorInsteadOfTap) {
                    backgroundColor = TileColor.randomFilter(backgroundColor)
                }
                tapCounter++
            }

            if (observeColorInsteadOfTap) {
                AnimatedContent(
                    modifier = Modifier.align(Alignment.Center),
                    targetState = backgroundColor,
                    contentAlignment = Alignment.Center,
                    transitionSpec = {
                        if (!oppositeDirections) {
                            enterTransitions.values.toList()[enterTransitionIndex] with // Enter transition
                                    exitTransitions.values.toList()[exitTransitionIndex] using // Exit Transition
                                    SizeTransform(clip = false)
                        } else {
                            enterTransitions.values.toList()[enterTransitionIndex] with // Enter transition
                                    exitTransitionsOpposite.values.toList()[exitTransitionIndex] using
                                    SizeTransform(clip = false)
                        }
                    },
                ) { state ->
                    Clicker(
                        modifier = Modifier.align(Alignment.Center),
                        backgroundColor = state,
                        tapCounter = tapCounter,
                        onTap = onTap,
                    )
                }

            } else {

                AnimatedContent(
                    modifier = Modifier.align(Alignment.Center),
                    targetState = tapCounter,
                    contentAlignment = Alignment.Center,
                    transitionSpec = {
                        if (!oppositeDirections) {
                            enterTransitions.values.toList()[enterTransitionIndex] with // Enter transition
                                    exitTransitions.values.toList()[exitTransitionIndex] using // Exit Transition
                                    SizeTransform(clip = false)
                        } else {
                            enterTransitions.values.toList()[enterTransitionIndex] with // Enter transition
                                    exitTransitionsOpposite.values.toList()[exitTransitionIndex] using
                                    SizeTransform(clip = false)
                        }
                    },
                ) { state ->
                    Clicker(
                        modifier = Modifier.align(Alignment.Center),
                        backgroundColor = backgroundColor,
                        tapCounter = state,
                        onTap = onTap,
                    )
                }
            }
        }
    }
}

@Composable
fun Clicker(
    modifier: Modifier = Modifier,
    backgroundColor: Color,
    tapCounter: Int,
    onTap: () -> Unit,
) {
    Box(
        modifier
            .padding(Grid.Two)
            .size(100.dp)
            // Make it grow
//            .size(100.dp + tapCounter.dp)
            .background(backgroundColor, shape = CircleShape)
            .clip(CircleShape)
            .clickable {
                onTap()
            }

    ) {
        Text(
            modifier = Modifier.align(Alignment.Center),
            text = if (tapCounter == 0) "Click me!" else "$tapCounter",
            textAlign = TextAlign.Center,
            style = if (tapCounter == 0) MaterialTheme.typography.headlineSmall else MaterialTheme.typography.headlineMedium,
            color = if (backgroundColor.luminance() > 0.5f) Color.Black else Color.White.copy(alpha = 0.8f),
            fontWeight = if (tapCounter == 0) FontWeight.Normal else FontWeight.Bold,
        )

    }
}
