package com.example.musicplayer.presentation.screens.queue

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicplayer.domain.model.Song
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

private data class QueueViewModelState(
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
        return QueueUiState.Success(songs)
    }
}

class QueueViewModel() : ViewModel() {

    private val viewModelState = MutableStateFlow(
        QueueViewModelState()
    )

    val uiState = viewModelState
        .map(QueueViewModelState::toUiState)
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            viewModelState.value.toUiState()
        )
}