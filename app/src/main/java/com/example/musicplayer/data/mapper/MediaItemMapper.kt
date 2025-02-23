package com.example.musicplayer.data.mapper

import android.net.Uri
import androidx.annotation.OptIn
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.MediaMetadata.PICTURE_TYPE_FRONT_COVER
import androidx.media3.common.util.UnstableApi
import com.example.musicplayer.data.model.SongEntity

class MediaItemMapper {

    @OptIn(UnstableApi::class)
    fun mapToMediaItem(songEntity: SongEntity): MediaItem {
        val uri = Uri.fromFile(songEntity.file)
        val metadata = MediaMetadata.Builder().apply {
            setTitle(songEntity.title)
            setSubtitle(songEntity.subtitle)
            setDurationMs(songEntity.durationMs)
            setArtworkData(songEntity.albumCover, PICTURE_TYPE_FRONT_COVER)
        }.build()
        val mediaItem = MediaItem.Builder().apply {
            setUri(uri)
            setMediaMetadata(metadata)
        }.build()
        return mediaItem
    }

    @OptIn(UnstableApi::class)
    fun mapToSongEntity(mediaItem: MediaItem): SongEntity {
        val metadata = mediaItem.mediaMetadata
        val title = metadata.title?.toString() ?: "MediaItemMapper error"
        val subtitle = metadata.subtitle?.toString()
        val durationMs = metadata.durationMs ?: 0L
        val albumCover = metadata.artworkData
        val songEntity = SongEntity(title, subtitle, durationMs, albumCover)
        return songEntity
    }
}