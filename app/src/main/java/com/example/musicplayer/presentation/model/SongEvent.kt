package com.example.musicplayer.presentation.model

sealed class SongEvent {
    data object PlayPauseSongToggle : SongEvent()
    data object SkipToNextSong : SongEvent()
    data object SkipToPreviousSong : SongEvent()
    data object ForwardSong : SongEvent()
    data object RewindSong : SongEvent()
    data class SeekSongToPosition(val position: Long) : SongEvent()
}