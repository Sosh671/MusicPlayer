package com.example.musicplayer.di

import com.example.musicplayer.domain.usecase.GetCurrentQueueStreamUseCase
import com.example.musicplayer.domain.usecase.GetCurrentSongStreamUseCase
import com.example.musicplayer.domain.usecase.PlayQueueSongUseCase
import com.example.musicplayer.presentation.screens.queue.QueueViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val queueModule = module {
    // UseCase
    factory { GetCurrentQueueStreamUseCase(get()) }
    factory { GetCurrentSongStreamUseCase(get()) }
    factory { PlayQueueSongUseCase(get()) }

    // ViewModel
    viewModel {
        QueueViewModel(
            getCurrentQueueStreamUseCase = get(),
            getCurrentSongStreamUseCase = get(),
            playQueueSongUseCase = get()
        )
    }
}