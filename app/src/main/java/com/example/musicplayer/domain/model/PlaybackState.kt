package com.example.musicplayer.domain.model

data class PlaybackState(
    val currentSong: Song?,
    val currentPositionMs: Long,
    val isPlaying: Boolean
)