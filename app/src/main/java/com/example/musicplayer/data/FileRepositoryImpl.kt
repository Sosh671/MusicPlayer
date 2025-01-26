package com.example.musicplayer.data

import com.example.musicplayer.domain.FileRepository
import java.io.File

class FileRepositoryImpl : FileRepository {

    private val supportedExtensions = listOf("mp3", "flac", "wav", "mp4")

    override fun getFiles(currentPath: File): List<File> {
        return currentPath.listFiles()?.filter {
            it.isDirectory || supportedExtensions.any { ext ->
                it.extension.equals(
                    ext,
                    ignoreCase = true
                )
            }
        } ?: emptyList()
    }
}