package com.example.musicplayer.presentation.filepicker

import java.io.File


sealed interface FilePickerUiState {
    data class Success(val files: List<File>) : FilePickerUiState
    data class Error(val errorMessage: String) : FilePickerUiState
    data object Empty : FilePickerUiState
}