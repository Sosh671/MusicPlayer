package com.example.musicplayer.data.repository

import android.content.ComponentName
import android.content.Context
import android.net.Uri
import androidx.annotation.OptIn
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.MediaMetadata.PICTURE_TYPE_FRONT_COVER
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.example.musicplayer.data.model.PlaybackStateEntity
import com.example.musicplayer.data.service.PlaybackService
import com.example.musicplayer.domain.model.PlaybackState
import com.example.musicplayer.domain.model.Song
import com.example.musicplayer.domain.repository.SongRepository
import com.example.musicplayer.mappers.PlaybackStateMapper
import com.example.musicplayer.mappers.SongMapper
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.MoreExecutors
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

const val POSITION_UPDATE_INTERVAL = 1000L
const val REWIND_INTERVAL = 5000L

class SongRepositoryImpl(
    context: Context,
    private val songMapper: SongMapper,
    private val playbackStateMapper: PlaybackStateMapper
) : SongRepository, KoinComponent {

    private val player by inject<ExoPlayer>()
    private val playbackStateEntityFlow: MutableStateFlow<PlaybackStateEntity> =
        MutableStateFlow(PlaybackStateEntity(0, false))

    private var mediaControllerFuture: ListenableFuture<MediaController>? = null
    private var job: Job? = null

    init {
        val componentName = ComponentName(context, PlaybackService::class.java)
        val sessionToken = SessionToken(context, componentName)
        mediaControllerFuture = MediaController.Builder(context, sessionToken).buildAsync()
        mediaControllerFuture?.addListener({ observePlayer() }, MoreExecutors.directExecutor())
    }

    override fun playSong(song: Song) {
        val songEntity = songMapper.mapToEntity(song)
        val uri = Uri.fromFile(songEntity.file)
        val metadata = MediaMetadata.Builder().apply {
            setTitle(songEntity.title)
            setSubtitle(songEntity.subtitle)
            setArtworkData(songEntity.albumCover, PICTURE_TYPE_FRONT_COVER)
        }.build()
        val mediaItem = MediaItem.Builder().apply {
            setUri(uri)
            setMediaMetadata(metadata)
        }.build()
        player.setMediaItem(mediaItem)
        player.prepare()
        player.play()
    }

    private fun observePlayer() {
        player.addListener(object : Player.Listener {
            @OptIn(UnstableApi::class)
            override fun onPlaybackStateChanged(playbackState: Int) {
                super.onPlaybackStateChanged(playbackState)
                if (playbackState == Player.STATE_READY) {
                    job?.cancel()
                    job = CoroutineScope(Dispatchers.Main).launch {
                        while (isActive) {
                            val position = player.currentPosition
                            val isPlaying = player.isPlaying
                            val state = PlaybackStateEntity(position, isPlaying)
                            playbackStateEntityFlow.value = state

                            delay(POSITION_UPDATE_INTERVAL)
                        }
                    }
                } else if (playbackState == Player.STATE_ENDED) {
                    job?.cancel()

                    val position = player.mediaMetadata.durationMs ?: player.currentPosition
                    val isPlaying = player.isPlaying
                    val state = PlaybackStateEntity(position, isPlaying)
                    playbackStateEntityFlow.value = state
                }
            }
        })
    }

    override fun resumeSong() = player.play()
    override fun pauseSong() = player.pause()

    override fun forwardSong() {
        val position = player.currentPosition + REWIND_INTERVAL
        seekSongToPosition(position)
    }

    override fun rewindSong() {
        val position = player.currentPosition - REWIND_INTERVAL
        if (position > 0) {
            player.seekTo(position)
        } else {
            player.seekTo(0)
        }
    }

    override fun seekSongToPosition(position: Long) = player.seekTo(position)

    override fun skipToNextSong() {
        // todo implement queue
    }

    override fun skipToPreviousSong() {
        // todo implement queue
    }

    override fun getPlaybackState(): Flow<PlaybackState> =
        playbackStateEntityFlow.map(playbackStateMapper::mapToDomain)

    override fun freeResources() {
        mediaControllerFuture?.let { MediaController.releaseFuture(it) }
        mediaControllerFuture = null
        job?.cancel()
        job = null
    }
}