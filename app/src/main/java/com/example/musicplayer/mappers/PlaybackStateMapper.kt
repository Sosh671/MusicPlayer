package com.example.musicplayer.mappers

import com.example.musicplayer.data.model.PlaybackStateEntity
import com.example.musicplayer.domain.model.PlaybackState

class PlaybackStateMapper : MapperInterface<PlaybackStateEntity, PlaybackState> {

    override fun mapToDomain(entity: PlaybackStateEntity): PlaybackState =
        PlaybackState(
            currentPositionMs = entity.currentPositionMs,
            isPlaying = entity.isPlaying
        )

    override fun mapToEntity(domain: PlaybackState): PlaybackStateEntity =
        PlaybackStateEntity(
            currentPositionMs = domain.currentPositionMs,
            isPlaying = domain.isPlaying
        )
}