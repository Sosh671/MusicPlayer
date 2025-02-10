package com.example.musicplayer.domain.model

import java.io.File

data class Song(
    val title: String,
    val subtitle: String?,
    val durationMs: Long,
    val albumCover: ByteArray?,
    val file: File? = null
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Song

        if (title != other.title) return false
        if (subtitle != other.subtitle) return false
        if (durationMs != other.durationMs) return false
        if (albumCover != null) {
            if (other.albumCover == null) return false
            if (!albumCover.contentEquals(other.albumCover)) return false
        } else if (other.albumCover != null) return false
        if (file != other.file) return false

        return true
    }

    override fun hashCode(): Int {
        var result = title.hashCode()
        result = 31 * result + (subtitle?.hashCode() ?: 0)
        result = 31 * result + durationMs.hashCode()
        result = 31 * result + (albumCover?.contentHashCode() ?: 0)
        result = 31 * result + (file?.hashCode() ?: 0)
        return result
    }
}