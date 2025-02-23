package com.example.musicplayer.presentation.screens.queue

import com.example.musicplayer.domain.model.Song

sealed interface QueueUiState {
    data class Success(val songs: List<Song>) : QueueUiState
    data class Error(val errorMessage: String) : QueueUiState
    data object Empty : QueueUiState
}