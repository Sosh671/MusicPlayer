package com.example.musicplayer.presentation

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.musicplayer.presentation._utils.Screens
import com.example.musicplayer.presentation.filepicker.FilePickerScreen
import com.example.musicplayer.presentation.theme.MusicPlayerTheme

@Composable
fun MusicPlayerApp() {
    val navController = rememberNavController()
    MusicPlayerNavHost(navController)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MusicPlayerNavHost(navController: NavHostController) {
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
                composable(filePicker.route) {
                    FilePickerScreen()
                }
                composable(player.route) {

                }
                composable(queue.route) {

                }
            }
        }
    }
}