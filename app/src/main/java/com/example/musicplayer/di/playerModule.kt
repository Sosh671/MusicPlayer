package com.example.musicplayer.di

import com.example.musicplayer.presentation.screens.player.PlayerSharedViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val playerModule = module {
    // ViewModel
    viewModel { PlayerSharedViewModel() }
}