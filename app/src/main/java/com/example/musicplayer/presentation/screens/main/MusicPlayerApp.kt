package com.example.musicplayer.presentation.screens.main

import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.QueueMusic
import androidx.compose.material.icons.automirrored.outlined.QueueMusic
import androidx.compose.material.icons.filled.FolderOpen
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.outlined.FolderOpen
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import com.example.musicplayer.presentation.model.PagerScreen
import com.example.musicplayer.presentation.screens.filepicker.FilePickerScreen
import com.example.musicplayer.presentation.screens.main.component.MusicPlayerPager
import com.example.musicplayer.presentation.screens.player.PlayerScreen
import com.example.musicplayer.presentation.screens.queue.QueueScreen
import com.example.musicplayer.presentation.theme.MusicPlayerTheme
import kotlinx.coroutines.launch

@Composable
fun MusicPlayerApp() {
    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState(pageCount = { 3 })
    val pagerScreens = listOf(
        PagerScreen(
            unselectedIcon = Icons.Outlined.FolderOpen,
            selectedIcon = Icons.Filled.FolderOpen,
            screenContent = {
                FilePickerScreen {
                    scope.launch {
                        // Navigate to the player screen
                        pagerState.animateScrollToPage(1)
                    }
                }
            }),
        PagerScreen(
            unselectedIcon = Icons.Outlined.PlayArrow,
            selectedIcon = Icons.Filled.PlayArrow,
            screenContent = { PlayerScreen() }),
        PagerScreen(
            unselectedIcon = Icons.AutoMirrored.Outlined.QueueMusic,
            selectedIcon = Icons.AutoMirrored.Filled.QueueMusic,
            screenContent = { QueueScreen() })
    )
    MusicPlayerTheme {
        Scaffold { innerPadding ->
            MusicPlayerPager(pagerState, innerPadding, pagerScreens)
        }
    }
}
