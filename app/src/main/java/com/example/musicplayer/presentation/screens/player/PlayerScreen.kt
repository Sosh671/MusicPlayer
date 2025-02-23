package com.example.musicplayer.presentation.screens.player

import android.content.res.Configuration
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Forward5
import androidx.compose.material.icons.rounded.Replay5
import androidx.compose.material.icons.rounded.SkipNext
import androidx.compose.material.icons.rounded.SkipPrevious
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.musicplayer.R
import com.example.musicplayer.domain.model.Song
import com.example.musicplayer.presentation._utils.toBitmap
import com.example.musicplayer.presentation._utils.toGradientColors
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
    val body = stringResource(R.string.please_choose_a_song)
    Box(modifier = Modifier.fillMaxSize()) {
        Text(
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.bodyLarge,
            text = body
        )
    }
}

@Composable
private fun Error(message: String) {
    val body = stringResource(R.string.error_args, message)
    Box(modifier = Modifier.fillMaxSize()) {
        Text(
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.bodyLarge,
            text = body
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
    val bitmap = remember { mutableStateOf(song.albumCover?.toBitmap()) }
    val iconResId =
        if (isSongPlaying) R.drawable.ic_round_pause
        else R.drawable.ic_round_play

    val gradientColors = bitmap.value?.toGradientColors() ?: listOf(
        Color.Transparent,
        MaterialTheme.colorScheme.background
    )
    val colorStops = listOf(0.0f to gradientColors[0], 0.8f to gradientColors[1]).toTypedArray()
    val defaultSubtitle = stringResource(R.string.unknown_artist)

    LaunchedEffect(song.albumCover) {
        bitmap.value = song.albumCover?.toBitmap()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(colorStops = colorStops))
    ) {
        Column(modifier = Modifier.padding(horizontal = 24.dp)) {
            Box(modifier = Modifier.padding(vertical = 32.dp)) {
                Crossfade(bitmap.value) { targetBitmap ->
                    AnimatedVinyl(
                        isSongPlaying = isSongPlaying,
                        bitmap = targetBitmap?.asImageBitmap()
                    )
                }
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
            Error(message = "Error loading song")
        }
    }
}