package com.example.musicplayer.presentation._utils

import android.graphics.Bitmap
import androidx.compose.ui.graphics.Color
import androidx.palette.graphics.Palette

fun Bitmap.toGradientColors(): List<Color> {
    val palette = Palette.from(this).generate()
    val defaultTransparentColor = 0x00000000
    val dominantColor = palette.getDominantColor(defaultTransparentColor)
    val color = Color(dominantColor)

    return listOf(color, Color.Transparent)
}