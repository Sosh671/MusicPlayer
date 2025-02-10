package com.example.musicplayer.domain.usecase

import com.example.musicplayer.domain.repository.SongRepository

class PauseSongUseCase(private val repository: SongRepository) {
    operator fun invoke() = repository.pauseSong()
}