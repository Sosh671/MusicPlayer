package com.example.musicplayer.di

import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import com.example.musicplayer.data.mapper.MediaItemMapper
import com.example.musicplayer.mappers.PlaybackStateMapper
import com.example.musicplayer.mappers.SongMapper
import com.example.musicplayer.data.repository.SongRepositoryImpl
import com.example.musicplayer.domain.repository.SongRepository
import com.example.musicplayer.domain.usecase.ForwardSongUseCase
import com.example.musicplayer.domain.usecase.FreePlaybackResourcesUseCase
import com.example.musicplayer.domain.usecase.GetPlaybackStateStreamUseCase
import com.example.musicplayer.domain.usecase.PauseSongUseCase
import com.example.musicplayer.domain.usecase.PlaySongUseCase
import com.example.musicplayer.domain.usecase.ResumeSongUseCase
import com.example.musicplayer.domain.usecase.RewindSongUseCase
import com.example.musicplayer.domain.usecase.SeekSongToPositionUseCase
import com.example.musicplayer.domain.usecase.SkipToNextSongUseCase
import com.example.musicplayer.domain.usecase.SkipToPreviousSongUseCase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val songModule = module {
    // ExoPlayer
    single { ExoPlayer.Builder(androidContext()).build() }
    single {
        val exoPlayer: ExoPlayer = get()
        MediaSession.Builder(androidContext(), exoPlayer).build()
    }

    // Mapper
    factory { SongMapper() }
    factory { PlaybackStateMapper(get()) }
    factory { MediaItemMapper() }

    // Repository
    single<SongRepository> { SongRepositoryImpl(androidContext(), get(), get(), get()) }

    // Use Case
    factory { PlaySongUseCase(get()) }
    factory { PauseSongUseCase(get()) }
    factory { ResumeSongUseCase(get()) }
    factory { GetPlaybackStateStreamUseCase(get()) }
    factory { ForwardSongUseCase(get()) }
    factory { RewindSongUseCase(get()) }
    factory { SkipToNextSongUseCase(get()) }
    factory { SkipToPreviousSongUseCase(get()) }
    factory { SeekSongToPositionUseCase(get()) }
    factory { FreePlaybackResourcesUseCase(get()) }
}