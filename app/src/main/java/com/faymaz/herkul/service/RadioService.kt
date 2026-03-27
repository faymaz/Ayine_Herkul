package com.faymaz.herkul.service

import android.app.*
import android.content.Intent
import android.os.Binder
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.faymaz.herkul.MainActivity
import com.faymaz.herkul.R
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.PlaybackException
import com.google.android.exoplayer2.Player

class RadioService : Service() {

    companion object {
        const val CHANNEL_ID = "herkul_radio_channel"
        const val NOTIFICATION_ID = 1001
        const val ACTION_PLAY = "com.faymaz.herkul.PLAY"
        const val ACTION_STOP = "com.faymaz.herkul.STOP"
        const val EXTRA_STREAM_URL = "stream_url"
        const val EXTRA_STATION_NAME = "station_name"
    }

    inner class RadioBinder : Binder() {
        fun getService(): RadioService = this@RadioService
    }

    private val binder = RadioBinder()
    private var player: ExoPlayer? = null
    private var currentStationName: String = ""
    private var isPlaying = false

    var onPlaybackStateChanged: ((Boolean) -> Unit)? = null
    var onError: ((String) -> Unit)? = null

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        initPlayer()
    }

    private fun initPlayer() {
        player = ExoPlayer.Builder(this).build()
        player?.addListener(object : Player.Listener {
            override fun onIsPlayingChanged(playing: Boolean) {
                isPlaying = playing
                onPlaybackStateChanged?.invoke(playing)
                if (playing) {
                    startForeground(NOTIFICATION_ID, buildNotification(currentStationName))
                } else {
                    stopForeground(false)
                }
            }

            override fun onPlayerError(error: PlaybackException) {
                onError?.invoke(error.message ?: "Bağlantı hatası")
            }
        })
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_PLAY -> {
                val url = intent.getStringExtra(EXTRA_STREAM_URL) ?: return START_NOT_STICKY
                val name = intent.getStringExtra(EXTRA_STATION_NAME) ?: ""
                play(url, name)
            }
            ACTION_STOP -> stop()
        }
        return START_NOT_STICKY
    }

    fun play(streamUrl: String, stationName: String) {
        currentStationName = stationName
        player?.apply {
            stop()
            setMediaItem(MediaItem.fromUri(streamUrl))
            prepare()
            playWhenReady = true
        }
    }

    fun stop() {
        player?.stop()
        stopForeground(true)
        isPlaying = false
        onPlaybackStateChanged?.invoke(false)
    }

    fun isPlaying() = isPlaying

    fun getCurrentStation() = currentStationName

    override fun onBind(intent: Intent?): IBinder = binder

    override fun onDestroy() {
        player?.release()
        player = null
        super.onDestroy()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Herkul Radyo",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Radyo çalma bildirimi"
            }
            getSystemService(NotificationManager::class.java)?.createNotificationChannel(channel)
        }
    }

    private fun buildNotification(stationName: String): Notification {
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val stopIntent = Intent(this, RadioService::class.java).apply {
            action = ACTION_STOP
        }
        val stopPending = PendingIntent.getService(
            this, 1, stopIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Herkul")
            .setContentText(stationName)
            .setSmallIcon(R.drawable.ic_radio)
            .setContentIntent(pendingIntent)
            .addAction(android.R.drawable.ic_media_pause, "Durdur", stopPending)
            .setOngoing(true)
            .build()
    }
}
