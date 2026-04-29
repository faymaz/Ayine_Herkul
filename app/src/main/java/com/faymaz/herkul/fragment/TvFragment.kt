package com.faymaz.herkul.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.faymaz.herkul.R
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.PlaybackException
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.ui.StyledPlayerView
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource

class TvFragment : Fragment() {

    companion object {
        private const val HLS_URL =
            "https://takeoff.jetstre.am/?account=mceutv&file=mc2" +
            "&type=live&service=wowza&protocol=https&output=playlist.m3u8"
    }

    private var player: ExoPlayer? = null
    private lateinit var playerView: StyledPlayerView
    private lateinit var loadingLayout: LinearLayout
    private lateinit var errorLayout: LinearLayout
    private lateinit var tvErrorText: TextView
    private lateinit var btnRetry: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_tv, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        playerView   = view.findViewById(R.id.playerView)
        loadingLayout = view.findViewById(R.id.loadingLayout)
        errorLayout  = view.findViewById(R.id.errorLayout)
        tvErrorText  = view.findViewById(R.id.tvErrorText)
        btnRetry     = view.findViewById(R.id.btnRetry)

        btnRetry.setOnClickListener { startPlayback() }
    }

    override fun onStart() {
        super.onStart()
        initPlayer()
    }

    override fun onStop() {
        super.onStop()
        releasePlayer()
    }

    private fun initPlayer() {
        player = ExoPlayer.Builder(requireContext()).build().also { exo ->
            playerView.player = exo
            exo.addListener(object : Player.Listener {
                override fun onPlaybackStateChanged(playbackState: Int) {
                    when (playbackState) {
                        Player.STATE_BUFFERING -> showLoading()
                        Player.STATE_READY     -> showPlayer()
                        Player.STATE_IDLE,
                        Player.STATE_ENDED     -> {}
                    }
                }

                override fun onPlayerError(error: PlaybackException) {
                    showError(error.message ?: getString(R.string.tv_error))
                }
            })
        }
        startPlayback()
    }

    private fun startPlayback() {
        showLoading()
        val hlsSource = HlsMediaSource.Factory(DefaultHttpDataSource.Factory())
            .createMediaSource(MediaItem.fromUri(HLS_URL))
        player?.apply {
            setMediaSource(hlsSource)
            prepare()
            playWhenReady = true
        }
    }

    private fun releasePlayer() {
        player?.release()
        player = null
    }

    private fun showLoading() {
        loadingLayout.visibility = View.VISIBLE
        errorLayout.visibility   = View.GONE
    }

    private fun showPlayer() {
        loadingLayout.visibility = View.GONE
        errorLayout.visibility   = View.GONE
    }

    private fun showError(message: String) {
        loadingLayout.visibility = View.GONE
        errorLayout.visibility   = View.VISIBLE
        tvErrorText.text         = message
    }
}
