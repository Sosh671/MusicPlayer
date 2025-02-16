package com.example.musicplayer.presentation.screens.main

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.QueueMusic
import androidx.compose.material.icons.automirrored.outlined.QueueMusic
import androidx.compose.material.icons.filled.FolderOpen
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.outlined.FolderOpen
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.musicplayer.presentation._utils.Screens
import com.example.musicplayer.presentation.model.PagerScreen
import com.example.musicplayer.presentation.screens.filepicker.FilePickerScreen
import com.example.musicplayer.presentation.screens.main.component.MusicPlayerPager
import com.example.musicplayer.presentation.screens.player.PlayerScreen
import com.example.musicplayer.presentation.screens.queue.QueueScreen
import com.example.musicplayer.presentation.theme.MusicPlayerTheme
import kotlinx.coroutines.launch

@Composable
fun MusicPlayerApp() {
//    val navController = rememberNavController()
//    MusicPlayerNavHost(navController)
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

// todo remove if unused
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MusicPlayerNavHost(navController: NavHostController) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scrollBehavior = BottomAppBarDefaults.exitAlwaysScrollBehavior()
    MusicPlayerTheme {
        Scaffold(
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
            snackbarHost = {
                SnackbarHost(
                    hostState = snackbarHostState,
                    // If a snackbar received a tap gesture - dismiss it
                    modifier = Modifier.pointerInput(Unit) { detectTapGestures { snackbarHostState.currentSnackbarData?.dismiss() } }
                )
            },
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = Screens.FilePicker.route
            ) {
                val filePicker = Screens.FilePicker
                val player = Screens.Player
                val queue = Screens.Queue
                composable(filePicker.route) {}
                composable(player.route) {}
                composable(queue.route) {}
            }
        }
    }
}
