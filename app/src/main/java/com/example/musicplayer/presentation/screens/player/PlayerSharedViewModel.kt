package com.example.musicplayer.presentation.screens.player

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicplayer._utils.MetadataRetriever
import com.example.musicplayer.domain.model.Song
import com.example.musicplayer.domain.usecase.ForwardSongUseCase
import com.example.musicplayer.domain.usecase.FreePlaybackResourcesUseCase
import com.example.musicplayer.domain.usecase.GetPlaybackStateStreamUseCase
import com.example.musicplayer.domain.usecase.PauseSongUseCase
import com.example.musicplayer.domain.usecase.PlaySongUseCase
import com.example.musicplayer.domain.usecase.ResumeSongUseCase
import com.example.musicplayer.domain.usecase.RewindSongUseCase
import com.example.musicplayer.domain.usecase.SeekSongToPositionUseCase
import com.example.musicplayer.domain.usecase.SkipToNextSongUseCase
import com.example.musicplayer.domain.usecase.SkipToPreviousSongUseCase
import com.example.musicplayer.presentation.model.SongEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File

// todo review complex object passing
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

class PlayerSharedViewModel(
    private val metadataRetriever: MetadataRetriever,
    private val getPlaybackStateStreamUseCase: GetPlaybackStateStreamUseCase,
    private val playSongUseCase: PlaySongUseCase,
    private val pauseSongUseCase: PauseSongUseCase,
    private val resumeSongUseCase: ResumeSongUseCase,
    private val forwardSongUseCase: ForwardSongUseCase,
    private val rewindSongUseCase: RewindSongUseCase,
    private val skipToNextSongUseCase: SkipToNextSongUseCase,
    private val skipToPreviousSongUseCase: SkipToPreviousSongUseCase,
    private val seekSongToPositionUseCase: SeekSongToPositionUseCase,
    private val freePlaybackResourcesUseCase: FreePlaybackResourcesUseCase
) : ViewModel() {

    init {
        // todo check dispatcher
        viewModelScope.launch(Dispatchers.Main) {
            getPlaybackStateStreamUseCase().collect { newState ->
                val song = newState.currentSong
                val position = newState.currentPositionMs
                val duration = newState.currentSong?.durationMs ?: 0L
                val isPlaying = newState.isPlaying
                viewModelState.update {
                    it.copy(
                        currentSong = song,
                        currentPosition = position,
                        totalDuration = duration,
                        isPlaying = isPlaying)
                }
            }
        }
    }

    private val viewModelState = MutableStateFlow(PlayerViewModelState())
    val uiState = viewModelState
        .map(PlayerViewModelState::toUiState)
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            viewModelState.value.toUiState()
        )

    fun freePlaybackResources() = freePlaybackResourcesUseCase()

    fun onEvent(event: SongEvent) {
        when (event) {
            is SongEvent.PlaySong -> playSong(event.file, event.queue)
            is SongEvent.PlayPauseSongToggle -> playPauseSongToggle()
            is SongEvent.ForwardSong -> forwardSong()
            is SongEvent.RewindSong -> rewindSong()
            is SongEvent.SeekSongToPosition -> seekSongToPosition(event.position)
            is SongEvent.SkipToNextSong -> skipToNextSong()
            is SongEvent.SkipToPreviousSong -> skipToPreviousSong()
        }
    }

    private fun playSong(file: File, queue: List<File>) {
        val currentSong = metadataRetriever.parseFileMetadata(file)
        val songQueue = queue.mapNotNull { metadataRetriever.parseFileMetadata(it) }
        val startIndex = songQueue.indexOf(currentSong)
        if (currentSong == null || startIndex == -1) {
            viewModelState.update {
                it.copy(errorMessage = "Failed to parse metadata for file: ${file.name}")
            }
            return
        }
        viewModelState.update {
            PlayerViewModelState(
                isPlaying = true,
                currentSong = currentSong,
                totalDuration = currentSong.durationMs,
                currentPosition = 0L
            )
        }
        playSongUseCase(startIndex, songQueue)
    }

    private fun playPauseSongToggle() {
        if (viewModelState.value.isPlaying) {
            pauseSongUseCase()
            viewModelState.update { it.copy(isPlaying = false) }
        } else {
            resumeSongUseCase()
            viewModelState.update { it.copy(isPlaying = true) }
        }
    }

    private fun forwardSong() = forwardSongUseCase()
    private fun rewindSong() = rewindSongUseCase()
    private fun skipToNextSong() = skipToNextSongUseCase()
    private fun skipToPreviousSong() = skipToPreviousSongUseCase()
    private fun seekSongToPosition(position: Long) = seekSongToPositionUseCase(position)
}