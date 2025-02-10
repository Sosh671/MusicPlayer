package com.example.musicplayer.domain.usecase

import com.example.musicplayer.domain.repository.SongRepository

class ResumeSongUseCase(private val repository: SongRepository) {
    operator fun invoke() = repository.resumeSong()
}