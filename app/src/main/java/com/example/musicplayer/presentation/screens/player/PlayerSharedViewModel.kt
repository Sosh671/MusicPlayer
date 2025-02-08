package com.example.musicplayer.presentation.screens.player

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicplayer.data.model.Song
import com.example.musicplayer.presentation.model.SongEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

private data class PlayerViewModelState(
    val isPlaying: Boolean = false,
    val currentSong: Song? = null,
    val currentPosition: Long = 0L,
    val totalDuration: Long = 0L,
    val errorMessage: String? = null,
) {
    fun toUiState(): PlayerUiState {
        if (errorMessage != null) {
            return PlayerUiState.Error(errorMessage)
        }
        if (currentSong == null) {
            return PlayerUiState.Empty
        }
        return PlayerUiState.Success(isPlaying, currentSong, currentPosition, totalDuration)
    }
}

class PlayerSharedViewModel : ViewModel() {

    private val viewModelState = MutableStateFlow(
        PlayerViewModelState()
    )

    val uiState = viewModelState
        .map(PlayerViewModelState::toUiState)
        .catch { exception ->
            viewModelState.update {
                it.copy(errorMessage = exception.localizedMessage)
            }
        }
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            viewModelState.value.toUiState()
        )

    fun onEvent(event: SongEvent) {

    }
}