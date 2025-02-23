package com.example.musicplayer.presentation.screens.queue

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.musicplayer.R
import com.example.musicplayer.domain.model.Song
import com.example.musicplayer.presentation.screens.queue.component.ListItemQueue
import com.example.musicplayer.presentation.theme.MusicPlayerTheme
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QueueScreen(
    viewModel: QueueViewModel = koinViewModel(),
    onSongSelected: (Song) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val title = stringResource(R.string.screens_queue_explorer_title)

    Column {
        TopAppBar(
            title = {
                Text(title, fontSize = 20.sp)
            }
        )
        when (uiState) {
            is QueueUiState.Empty -> Empty()
            is QueueUiState.Error -> {
                val state = uiState as? QueueUiState.Error ?: return@Column
                Error(state.errorMessage)
            }

            is QueueUiState.Success -> {
                val state = uiState as? QueueUiState.Success ?: return@Column
                QueueList(state.songs) { song ->

                }
            }
        }
    }
}

@Composable
private fun Empty() {
    val body = stringResource(R.string.the_queue_is_empty)
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
private fun QueueList(songs: List<Song>, onClick: (Song) -> Unit) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(songs) { song ->
            ListItemQueue(song) {
                onClick(song)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun QueueScreenPreview() {
    val song1 = Song("Title", "Subtitle", 0L, byteArrayOf())
    val song2 = Song("Title 2", "Subtitle 2", 0L, byteArrayOf())
    val song3 = Song("Title 3", "Subtitle 3", 0L, byteArrayOf())
    val songs = listOf(song1, song2, song3)
    MusicPlayerTheme {
        Surface {
            Column {
                TopAppBar(
                    title = {
                        Text("Queue", fontSize = 20.sp)
                    }
                )
                QueueList(songs) {}
            }
        }
    }
}

@Preview
@Composable
private fun QueueScreenEmptyPreview() {
    MusicPlayerTheme {
        Surface {
            Empty()
        }
    }
}

@Preview
@Composable
private fun QueueScreenErrorPreview() {
    MusicPlayerTheme {
        Surface {
            Error("Error while loading queue")
        }
    }
}