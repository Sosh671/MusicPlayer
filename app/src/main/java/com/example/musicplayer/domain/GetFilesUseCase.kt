package com.example.musicplayer.domain

import java.io.File

class GetFilesUseCase(private val fileRepository: FileRepository) {
    operator fun invoke(currentPath: File): List<File> = fileRepository.getFiles(currentPath)
}