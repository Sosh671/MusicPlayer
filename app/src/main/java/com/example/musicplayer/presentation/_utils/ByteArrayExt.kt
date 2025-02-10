package com.example.musicplayer.presentation._utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log

fun ByteArray.toBitmap(): Bitmap? = try {
    BitmapFactory.decodeByteArray(this, 0, this.size)
} catch (e: Exception) {
    Log.d("TAG", "ByteArray.toBitmap -> exception: $e")
    null
}