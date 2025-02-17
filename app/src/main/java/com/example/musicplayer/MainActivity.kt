package com.example.musicplayer

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.example.musicplayer.presentation.screens.filepicker.FilePickerSharedViewModel
import com.example.musicplayer.presentation.screens.main.MusicPlayerApp
import com.example.musicplayer.presentation.screens.player.PlayerSharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainActivity : ComponentActivity() {

    private val playerSharedViewModel by viewModel<PlayerSharedViewModel>()
    private val filePickerSharedViewModel by viewModel<FilePickerSharedViewModel>()
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                filePickerSharedViewModel.getFiles()
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                requestStoragePermission()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MusicPlayerApp()
        }
        if (!checkStoragePermissionGranted()) {
            requestStoragePermission()
        }
    }

    private fun checkStoragePermissionGranted(): Boolean =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_AUDIO) ==
                    PackageManager.PERMISSION_GRANTED
        } else {
            ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) ==
                    PackageManager.PERMISSION_GRANTED
        }

    private fun requestStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissionLauncher.launch(Manifest.permission.READ_MEDIA_AUDIO)
        } else {
            requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        playerSharedViewModel.freePlaybackResources()
    }
}