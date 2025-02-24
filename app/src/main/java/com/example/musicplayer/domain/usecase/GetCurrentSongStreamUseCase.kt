package com.example.musicplayer.domain.usecase

import com.example.musicplayer.domain.repository.SongRepository
import kotlinx.coroutines.flow.map

class GetCurrentSongStreamUseCase(private val repository: SongRepository) {
    operator fun invoke() = repository.getPlaybackStateStream().map { it.currentSong }
}