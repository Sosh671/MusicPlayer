package com.example.musicplayer.domain.usecase

import com.example.musicplayer.domain.repository.SongRepository

class SeekSongToPositionUseCase(private val repository: SongRepository) {
    operator fun invoke(position: Long) = repository.seekSongToPosition(position)
}