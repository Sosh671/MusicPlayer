package com.example.musicplayer.di

import com.example.musicplayer.presentation.screens.player.PlayerSharedViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val playerModule = module {
    // ViewModel
    viewModel {
        PlayerSharedViewModel(
            metadataRetriever = get(),
            getPlaybackStateUseCase = get(),
            playSongUseCase = get(),
            pauseSongUseCase = get(),
            resumeSongUseCase = get(),
            forwardSongUseCase = get(),
            rewindSongUseCase = get(),
            skipToNextSongUseCase = get(),
            skipToPreviousSongUseCase = get(),
            seekSongToPositionUseCase = get(),
            freePlaybackResourcesUseCase = get()
        )
    }
}