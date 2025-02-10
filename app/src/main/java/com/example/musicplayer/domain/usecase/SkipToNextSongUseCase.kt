package com.example.musicplayer.domain.usecase

import com.example.musicplayer.domain.repository.SongRepository

class SkipToNextSongUseCase(private val repository: SongRepository) {
    operator fun invoke() = repository.skipToNextSong()
}