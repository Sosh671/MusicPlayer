package com.example.musicplayer.data.model

data class PlaybackStateEntity(
    val currentSongEntity: SongEntity?,
    val currentPositionMs: Long,
    val isPlaying: Boolean
)