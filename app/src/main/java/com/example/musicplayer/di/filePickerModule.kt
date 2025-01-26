package com.example.musicplayer.di

import com.example.musicplayer.data.FileRepositoryImpl
import com.example.musicplayer.domain.FileRepository
import com.example.musicplayer.domain.GetFilesUseCase
import com.example.musicplayer.presentation.filepicker.FilePickerViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val filePickerModule = module {

    // Repository
    single<FileRepository> { FileRepositoryImpl() }

    // Use Case
    factory { GetFilesUseCase(get()) }

    // ViewModel
    viewModel { FilePickerViewModel(get()) }
}