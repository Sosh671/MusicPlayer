package com.example.musicplayer.domain.repository

import com.example.musicplayer.domain.model.PlaybackState
import com.example.musicplayer.domain.model.Song
import kotlinx.coroutines.flow.Flow

interface SongRepository {
    fun getPlaybackState(): Flow<PlaybackState>
    fun playSong(startIndex: Int, queue: List<Song>)
    fun resumeSong()
    fun pauseSong()
    fun forwardSong()
    fun rewindSong()
    fun seekSongToPosition(position: Long)
    fun skipToNextSong()
    fun skipToPreviousSong()

    fun freeResources()
}