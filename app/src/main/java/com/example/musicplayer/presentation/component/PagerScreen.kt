package com.example.musicplayer.presentation.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

data class PagerScreen(
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val screenContent: @Composable () -> Unit,
)