package com.example.musicplayer.di

import com.example.musicplayer.presentation.screens.queue.QueueViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val queueModule = module {
    // ViewModel
    viewModel { QueueViewModel() }
}