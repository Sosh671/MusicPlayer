package com.example.musicplayer.presentation.screens.player

import com.example.musicplayer.data.model.Song

sealed interface PlayerUiState {
    data class Success(
        val isPlaying: Boolean = false,
        val currentSong: Song,
        val currentPosition: Long = 0L,
        val totalDuration: Long = 0L
    ) : PlayerUiState

    data class Error(val errorMessage: String) : PlayerUiState
    data object Empty : PlayerUiState
}