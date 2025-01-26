package com.example.musicplayer.domain

import java.io.File

interface FileRepository {

    fun getFiles(currentPath: File): List<File>
}