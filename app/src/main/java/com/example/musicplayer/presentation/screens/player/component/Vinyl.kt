package com.example.musicplayer.presentation.screens.player.component

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.musicplayer.R
import com.example.musicplayer.presentation.theme.MusicPlayerTheme
import com.example.musicplayer.presentation.theme.roundedShape

private const val ANIMATION_PLAYING_DURATION = 7000
private const val ANIMATION_SETTLING_DURATION = 2000

@Composable
fun AnimatedVinyl(
    isSongPlaying: Boolean = true,
    bitmap: ImageBitmap? = null
) {
    var currentRotation by remember {
        mutableFloatStateOf(0f)
    }

    val rotation = remember {
        Animatable(currentRotation)
    }

    LaunchedEffect(isSongPlaying) {
        if (isSongPlaying) {
            rotation.animateTo(
                targetValue = currentRotation + 360f,
                animationSpec = infiniteRepeatable(
                    animation = tween(
                        durationMillis = ANIMATION_PLAYING_DURATION,
                        easing = LinearEasing
                    ),
                    repeatMode = RepeatMode.Restart
                )
            ) {
                currentRotation = value
            }
        } else {
            if (currentRotation > 0f) {
                rotation.animateTo(
                    targetValue = currentRotation + 50,
                    animationSpec = tween(
                        durationMillis = ANIMATION_SETTLING_DURATION,
                        easing = LinearOutSlowInEasing
                    )
                ) {
                    currentRotation = value
                }
            }
        }
    }

    Vinyl(bitmap = bitmap, rotationDegrees = rotation.value)
}

@Composable
private fun Vinyl(
    modifier: Modifier = Modifier,
    rotationDegrees: Float = 0f,
    bitmap: ImageBitmap? = null
) {
    Box(
        modifier = modifier
            .aspectRatio(1.0f)
            .clip(roundedShape)
    ) {
        // Vinyl background
        Image(
            modifier = Modifier
                .fillMaxSize()
                .rotate(rotationDegrees),
            painter = painterResource(id = R.drawable.vinyl_background),
            contentDescription = "Vinyl Background"
        )

        // Vinyl song cover
        if (bitmap == null) {
            val imagePainter = rememberVectorPainter(Icons.Rounded.PlayArrow)
            Image(
                modifier = Modifier
                    .fillMaxSize(0.6f)
                    .aspectRatio(1.0f)
                    .align(Alignment.Center)
                    .clip(roundedShape),
                painter = imagePainter,
                colorFilter = ColorFilter.tint(Color.White),
                contentDescription = "Song cover"
            )
        } else {
            Image(
                modifier = Modifier
                    .fillMaxSize(0.6f)
                    .rotate(rotationDegrees)
                    .aspectRatio(1.0f)
                    .align(Alignment.Center)
                    .clip(roundedShape),
                bitmap = bitmap,
                contentDescription = "Song cover"
            )
        }
    }
}

@Preview
@Composable
fun PreviewAnimatedVinyl() {
    MusicPlayerTheme {
        Surface {
            AnimatedVinyl()
        }
    }
}
