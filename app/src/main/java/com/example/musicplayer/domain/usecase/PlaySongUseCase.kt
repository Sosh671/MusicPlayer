package com.example.musicplayer.domain.usecase

import com.example.musicplayer.domain.model.Song
import com.example.musicplayer.domain.repository.SongRepository

class PlaySongUseCase(private val repository: SongRepository) {
    operator fun invoke(song: Song) = repository.playSong(song)
}