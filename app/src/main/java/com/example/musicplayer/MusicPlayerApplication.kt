package com.example.musicplayer

import android.app.Application
import com.example.musicplayer.di.filePickerModule
import com.example.musicplayer.di.playerModule
import com.example.musicplayer.di.songModule
import com.example.musicplayer.di.utilsModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MusicPlayerApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@MusicPlayerApplication)
            modules(
                utilsModule,
                filePickerModule,
                playerModule,
                songModule
            )
        }
    }
}