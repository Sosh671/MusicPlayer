package com.example.musicplayer.domain.repository

import com.example.musicplayer.domain.model.PlaybackState
import com.example.musicplayer.domain.model.Song
import kotlinx.coroutines.flow.Flow

interface SongRepository {
    fun getCurrentQueueStream(): Flow<List<Song>>
    fun getPlaybackStateStream(): Flow<PlaybackState>

    fun playSong(startIndex: Int, queue: List<Song>)
    fun playSong(queueIndex: Int)
    fun resumeSong()
    fun pauseSong()
    fun forwardSong()
    fun rewindSong()
    fun seekSongToPosition(position: Long)
    fun skipToNextSong()
    fun skipToPreviousSong()

    fun freeResources()
}