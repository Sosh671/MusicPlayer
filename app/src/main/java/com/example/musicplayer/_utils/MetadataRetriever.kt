package com.example.musicplayer._utils

import android.media.MediaMetadataRetriever
import android.media.MediaMetadataRetriever.METADATA_KEY_ARTIST
import android.media.MediaMetadataRetriever.METADATA_KEY_DURATION
import android.media.MediaMetadataRetriever.METADATA_KEY_TITLE
import android.util.Log
import com.example.musicplayer.domain.model.Song
import java.io.File

class MetadataRetriever {

    fun parseFileMetadata(file: File): Song? {
        val retriever = MediaMetadataRetriever()
        try {
            retriever.setDataSource(file.absolutePath)
            val title = retriever.extractMetadata(METADATA_KEY_TITLE) ?: file.nameWithoutExtension
            val subtitle = retriever.extractMetadata(METADATA_KEY_ARTIST)
            val duration = retriever.extractMetadata(METADATA_KEY_DURATION)?.toLongOrNull() ?: 0L
            val albumArt = retriever.embeddedPicture
            val song = Song(title, subtitle, duration, albumArt, file)
            return song
        } catch (e: Exception) {
            Log.d("TAG", "PlayerSharedViewModel.parseFileMetadata -> exception: " + e.message)
        }
        return null
    }
}