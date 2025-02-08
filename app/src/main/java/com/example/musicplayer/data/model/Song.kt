package com.example.musicplayer.data.model

import android.net.Uri

data class Song(
    val title: String,
    val subtitle: String,
    val coverUri: Uri
)