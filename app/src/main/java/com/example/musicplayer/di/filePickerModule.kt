package com.example.musicplayer.di

import com.example.musicplayer.data.repository.FileRepositoryImpl
import com.example.musicplayer.domain.repository.FileRepository
import com.example.musicplayer.domain.usecase.GetFilesUseCase
import com.example.musicplayer.presentation.screens.filepicker.FilePickerSharedViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val filePickerModule = module {

    // Repository
    single<FileRepository> { FileRepositoryImpl() }

    // Use Case
    factory { GetFilesUseCase(get()) }

    // ViewModel
    viewModel { FilePickerSharedViewModel(get()) }
}