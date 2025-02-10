package com.example.musicplayer.di

import com.example.musicplayer._utils.MetadataRetriever
import org.koin.dsl.module

val utilsModule = module {
    // Utils
    factory { MetadataRetriever() }
}