package com.example.musicplayer.mappers

import com.example.musicplayer.data.model.PlaybackStateEntity
import com.example.musicplayer.domain.model.PlaybackState

class PlaybackStateMapper(
    private val songMapper: SongMapper
) : MapperInterface<PlaybackStateEntity, PlaybackState> {

    override fun mapToDomain(entity: PlaybackStateEntity): PlaybackState =
        PlaybackState(
            currentSong = entity.currentSongEntity?.let { songMapper.mapToDomain(it) },
            currentPositionMs = entity.currentPositionMs,
            isPlaying = entity.isPlaying
        )

    override fun mapToEntity(domain: PlaybackState): PlaybackStateEntity =
        PlaybackStateEntity(
            currentSongEntity = domain.currentSong?.let { songMapper.mapToEntity(it) },
            currentPositionMs = domain.currentPositionMs,
            isPlaying = domain.isPlaying
        )
}