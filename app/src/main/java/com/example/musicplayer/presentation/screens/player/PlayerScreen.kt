package com.example.musicplayer.presentation.screens.player

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ProgressIndicatorDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Forward5
import androidx.compose.material.icons.rounded.Replay5
import androidx.compose.material.icons.rounded.SkipNext
import androidx.compose.material.icons.rounded.SkipPrevious
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.musicplayer.R
import com.example.musicplayer.domain.model.Song
import com.example.musicplayer.presentation._utils.toBitmap
import com.example.musicplayer.presentation._utils.toTime
import com.example.musicplayer.presentation.model.SongEvent
import com.example.musicplayer.presentation.screens.player.component.AnimatedVinyl
import com.example.musicplayer.presentation.theme.MusicPlayerTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun PlayerScreen(viewModel: PlayerSharedViewModel = koinViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Box {
        when (uiState) {
            is PlayerUiState.Empty -> Empty()
            is PlayerUiState.Error -> {
                val state = uiState as? PlayerUiState.Error ?: return@Box
                Error(state.errorMessage)
            }

            is PlayerUiState.Success -> {
                val state = uiState as? PlayerUiState.Success ?: return@Box
                SongScreenBody(
                    song = state.currentSong,
                    isSongPlaying = state.isPlaying,
                    currentPosition = state.currentPosition,
                    totalDuration = state.totalDuration,
                    onEvent = { viewModel.onEvent(it) }
                )
            }
        }
    }
}

@Composable
private fun Empty() {
    Box(modifier = Modifier.fillMaxSize()) {
        Text(
            modifier = Modifier.align(Alignment.Center),
            text = "No file found"
        )
    }
}

@Composable
private fun Error(message: String) {
    Box(modifier = Modifier.fillMaxSize()) {
        Text(
            modifier = Modifier.align(Alignment.Center),
            text = "Error: $message"
        )
    }
}

@Composable
fun SongScreenBody(
    song: Song,
    isSongPlaying: Boolean,
    currentPosition: Long,
    totalDuration: Long,
    onEvent: (SongEvent) -> Unit
) {
    val bitmap = remember { song.albumCover?.toBitmap() }
    val iconResId =
        if (isSongPlaying) R.drawable.ic_round_pause
        else R.drawable.ic_round_play

    // todo remove dark mode
    val gradientColors = if (isSystemInDarkTheme()) {
        listOf(Color.Transparent, MaterialTheme.colorScheme.background)
    } else {
        listOf(MaterialTheme.colorScheme.background, MaterialTheme.colorScheme.background)
    }

    val sliderColors = if (isSystemInDarkTheme()) {
        SliderDefaults.colors(
            thumbColor = MaterialTheme.colorScheme.onBackground,
            activeTrackColor = MaterialTheme.colorScheme.onBackground,
            inactiveTrackColor = MaterialTheme.colorScheme.onBackground.copy(
                alpha = ProgressIndicatorDefaults.IndicatorBackgroundOpacity
            ),
        )
    } else SliderDefaults.colors(
        thumbColor = Color.Transparent,
        activeTrackColor = Color.Transparent,
        inactiveTrackColor = Color.Transparent.copy(
            alpha = ProgressIndicatorDefaults.IndicatorBackgroundOpacity
        ),
    )
    val defaultSubtitle = stringResource(R.string.unknown_artist)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = gradientColors,
                    endY = LocalConfiguration.current.screenHeightDp.toFloat() * LocalDensity.current.density
                )
            )
    ) {
        Column(modifier = Modifier.padding(horizontal = 24.dp)) {
            Box(modifier = Modifier.padding(vertical = 32.dp)) {
                AnimatedVinyl(isSongPlaying = isSongPlaying, bitmap = bitmap?.asImageBitmap())
            }

            Text(
                text = song.title,
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onBackground,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = song.subtitle ?: defaultSubtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onBackground,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.graphicsLayer {
                    alpha = 0.6f
                }
            )

            Slider(
                value = currentPosition.toFloat(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp),
                valueRange = 0f..totalDuration.toFloat(),
                colors = sliderColors,
                onValueChange = { newPosition ->
                    onEvent(SongEvent.SeekSongToPosition(newPosition.toLong()))
                },
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    currentPosition.toTime(),
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    totalDuration.toTime(),
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Row(
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
            ) {
                Icon(
                    imageVector = Icons.Rounded.SkipPrevious,
                    contentDescription = "Skip Previous",
                    modifier = Modifier
                        .clip(CircleShape)
                        .clickable(onClick = { onEvent(SongEvent.SkipToPreviousSong) })
                        .padding(12.dp)
                        .size(32.dp)
                )
                Icon(
                    imageVector = Icons.Rounded.Replay5,
                    contentDescription = "Replay 5 seconds",
                    modifier = Modifier
                        .clip(CircleShape)
                        .clickable(onClick = { onEvent(SongEvent.RewindSong) })
                        .padding(12.dp)
                        .size(32.dp)
                )
                Icon(
                    painter = painterResource(iconResId),
                    contentDescription = "Play/pause toggle",
                    tint = MaterialTheme.colorScheme.background,
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.onBackground)
                        .clickable(onClick = { onEvent(SongEvent.PlayPauseSongToggle) })
                        .size(64.dp)
                        .padding(8.dp)
                )
                Icon(
                    imageVector = Icons.Rounded.Forward5,
                    contentDescription = "Forward 5 seconds",
                    modifier = Modifier
                        .clip(CircleShape)
                        .clickable(onClick = { onEvent(SongEvent.ForwardSong) })
                        .padding(12.dp)
                        .size(32.dp)
                )
                Icon(
                    imageVector = Icons.Rounded.SkipNext,
                    contentDescription = "Skip Next",
                    modifier = Modifier
                        .clip(CircleShape)
                        .clickable(onClick = { onEvent(SongEvent.SkipToNextSong) })
                        .padding(12.dp)
                        .size(32.dp)
                )
            }
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PreviewMusicPlayerScreenDark() {
    val song = Song("Title", "Subtitle", 0L, byteArrayOf())
    MusicPlayerTheme {
        Surface {
            SongScreenBody(
                song = song,
                isSongPlaying = true,
                currentPosition = 200,
                totalDuration = 400,
                onEvent = { }
            )
        }
    }
}

@Preview
@Composable
private fun PreviewMusicPlayerScreen() {
    val song = Song("Title", "Subtitle", 0L, byteArrayOf())
    MusicPlayerTheme {
        Surface {
            SongScreenBody(
                song = song,
                isSongPlaying = true,
                currentPosition = 200,
                totalDuration = 400,
                onEvent = { }
            )
        }
    }
}

@Preview
@Composable
private fun PreviewPlayerScreenEmpty() {
    MusicPlayerTheme {
        Surface {
            Empty()
        }
    }
}

@Preview
@Composable
private fun PreviewPlayerScreenError() {
    MusicPlayerTheme {
        Surface {
            Error(message = "Error loading file")
        }
    }
}