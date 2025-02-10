package com.example.musicplayer.mappers

import com.example.musicplayer.data.model.SongEntity
import com.example.musicplayer.domain.model.Song

class SongMapper: MapperInterface<SongEntity, Song> {

    override fun mapToDomain(entity: SongEntity): Song =
        Song(
            title = entity.title,
            subtitle = entity.subtitle,
            durationMs = entity.durationMs,
            albumCover = entity.albumCover,
            file = entity.file
        )

    override fun mapToEntity(domain: Song): SongEntity =
        SongEntity(
            title = domain.title,
            subtitle = domain.subtitle,
            durationMs = domain.durationMs,
            albumCover = domain.albumCover,
            file = domain.file
        )
}