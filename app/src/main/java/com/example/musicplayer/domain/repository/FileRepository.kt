package com.example.musicplayer.domain.repository

import java.io.File

interface FileRepository {
    fun getFiles(currentPath: File): List<File>
}