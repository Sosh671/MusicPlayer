package com.example.musicplayer.presentation.screens.queue

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicplayer.domain.model.Song
import com.example.musicplayer.domain.usecase.GetCurrentQueueStreamUseCase
import com.example.musicplayer.domain.usecase.GetCurrentSongStreamUseCase
import com.example.musicplayer.domain.usecase.PlayQueueSongUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

private data class QueueViewModelState(
    val currentSong: Song? = null,
    val songs: List<Song>? = null,
    val errorMessage: String? = null,
) {
    fun toUiState(): QueueUiState {
        if (errorMessage != null) {
            return QueueUiState.Error(errorMessage)
        }
        if (songs.isNullOrEmpty()) {
            return QueueUiState.Empty
        }
        return QueueUiState.Success(songs, songs.indexOf(currentSong))
    }
}

class QueueViewModel(
    private val getCurrentQueueStreamUseCase: GetCurrentQueueStreamUseCase,
    private val getCurrentSongStreamUseCase: GetCurrentSongStreamUseCase,
    private val playQueueSongUseCase: PlayQueueSongUseCase,
) : ViewModel() {

    private val viewModelState = MutableStateFlow(QueueViewModelState())
    val uiState = viewModelState
        .map(QueueViewModelState::toUiState)
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            viewModelState.value.toUiState()
        )

    init {
        viewModelScope.launch {
            getCurrentQueueStreamUseCase().collectLatest { songs ->
                viewModelState.update { it.copy(songs = songs) }
            }
        }
        viewModelScope.launch {
            getCurrentSongStreamUseCase().collectLatest { song ->
                viewModelState.update { it.copy(currentSong = song) }
            }
        }
    }

    fun playSong(index: Int) {
        playQueueSongUseCase(index)
    }
}