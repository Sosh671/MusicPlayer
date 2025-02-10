package com.example.musicplayer.domain.usecase

import com.example.musicplayer.domain.repository.SongRepository

class FreePlaybackResourcesUseCase(private val repository: SongRepository) {
    operator fun invoke() = repository.freeResources()
}