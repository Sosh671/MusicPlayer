package com.example.musicplayer.domain.usecase

import com.example.musicplayer.domain.model.PlaybackState
import com.example.musicplayer.domain.repository.SongRepository
import kotlinx.coroutines.flow.Flow

class GetPlaybackStateUseCase(private val songRepository: SongRepository) {
    operator fun invoke(): Flow<PlaybackState> = songRepository.getPlaybackState()
}