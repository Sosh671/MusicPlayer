package com.example.musicplayer.data.service

import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSession.ControllerInfo
import androidx.media3.session.MediaSessionService
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

class PlaybackService : MediaSessionService(), KoinComponent {

    private var player: ExoPlayer? = null
    private var mediaSession: MediaSession? = null

    override fun onCreate() {
        super.onCreate()
        player = get<ExoPlayer>()
        mediaSession = get<MediaSession>()
    }

    override fun onDestroy() {
        mediaSession?.run {
            player.release()
            release()
            mediaSession = null
        }
        super.onDestroy()
    }

    override fun onGetSession(controllerInfo: ControllerInfo): MediaSession? = mediaSession
}