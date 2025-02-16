package com.example.musicplayer.presentation.screens.filepicker

import android.os.Environment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicplayer.domain.usecase.GetFilesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import java.io.File

private data class FilePickerViewModelState(
    val files: List<File>? = null,
    val errorMessage: String? = null,
) {
    fun toUiState(): FilePickerUiState {
        if (errorMessage != null) {
            return FilePickerUiState.Error(errorMessage)
        }
        if (files.isNullOrEmpty()) {
            return FilePickerUiState.Empty
        }
        return FilePickerUiState.Success(files)
    }
}

class FilePickerViewModel(private val getFilesUseCase: GetFilesUseCase) : ViewModel() {

    private val viewModelState = MutableStateFlow(
        FilePickerViewModelState()
    )

    val uiState = viewModelState
        .map(FilePickerViewModelState::toUiState)
        .catch { exception ->
            viewModelState.update {
                it.copy(errorMessage = exception.localizedMessage)
            }
        }
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            viewModelState.value.toUiState()
        )

    private val rootDirectory = Environment.getExternalStorageDirectory()
    private var currentDirectory: File = rootDirectory

    init {
        val files = getFilesUseCase(rootDirectory)
        // todo check if the state updates with the error message
        viewModelState.update { it.copy(files = files) }
    }

    fun selectFile(file: File) {
        if (file.isDirectory) {
            changeDirectory(file)
        }
    }

    private fun changeDirectory(directory: File) {
        currentDirectory = directory
        val files = getFilesUseCase(directory)
        viewModelState.update { it.copy(files = files) }
    }

    fun navigateBack() {
        if (currentDirectory == rootDirectory) {
            return
        }
        changeDirectory(currentDirectory.parentFile ?: rootDirectory)
    }
}