package com.example.musicplayer.presentation.screens.filepicker

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.musicplayer.presentation.model.SongEvent
import com.example.musicplayer.presentation.screens.filepicker.component.ListItemDirectory
import com.example.musicplayer.presentation.screens.filepicker.component.ListItemFile
import com.example.musicplayer.presentation.screens.player.PlayerSharedViewModel
import com.example.musicplayer.presentation.theme.MusicPlayerTheme
import org.koin.androidx.compose.koinViewModel
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilePickerScreen(
    viewModel: FilePickerViewModel = koinViewModel(),
    sharedViewModel: PlayerSharedViewModel = koinViewModel(),
    onSongSelected: (File) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val listState = rememberLazyListState()

    // Handle system back button press using BackHandler
    BackHandler(enabled = true) {
        viewModel.navigateBack()
    }

    Column {
        TopAppBar(
            title = {
                Text("File Explorer", fontSize = 20.sp)
            }
        )
        when (uiState) {
            is FilePickerUiState.Empty -> Empty()
            is FilePickerUiState.Error -> Error((uiState as FilePickerUiState.Error).errorMessage)
            is FilePickerUiState.Success -> {
                val filesInDirectory = (uiState as FilePickerUiState.Success).files
                FilePickerList(filesInDirectory, listState) { file ->
                    viewModel.selectFile(file)
                    if (file.isFile) {
                        onSongSelected(file)
                        sharedViewModel.onEvent(SongEvent.PlaySong(file))
                    }
                }
            }
        }
    }
}

// todo test
@Composable
private fun Empty() {
    Text("No files found")
}

@Composable
private fun Error(message: String) {
    Toast.makeText(LocalContext.current, message, Toast.LENGTH_SHORT).show()
}

@Composable
private fun FilePickerList(files: List<File>, listState: LazyListState, onClick: (File) -> Unit) {
    LazyColumn(
        state = listState,
        modifier = Modifier.fillMaxSize()
    ) {
        itemsIndexed(files) { index, file ->
            if (file.isDirectory) {
                ListItemDirectory(file) {
                    onClick(file)
                }
            } else {
                ListItemFile(file) {
                    onClick(file)
                }
            }
        }
    }
    LaunchedEffect(files) {
        listState.scrollToItem(0)
    }
}

@Preview
@Composable
private fun FilePickerScreenPreview() {
    val files = listOf(File("song1.mp3"), File("song2.flac"), File("song3.wav"))
    val state = rememberLazyListState()
    MusicPlayerTheme {
        Surface {
            FilePickerList(files, state) {}
        }
    }
}