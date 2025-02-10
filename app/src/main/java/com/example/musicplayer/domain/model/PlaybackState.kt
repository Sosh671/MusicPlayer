package com.example.musicplayer.domain.model

data class PlaybackState(
    val currentPositionMs: Long,
    val isPlaying: Boolean
)