package com.example.musicplayer.domain.usecase

import com.example.musicplayer.domain.repository.SongRepository

class PlayQueueSongUseCase(private val repository: SongRepository) {
    operator fun invoke(queueIndex: Int) = repository.playSong(queueIndex)
}