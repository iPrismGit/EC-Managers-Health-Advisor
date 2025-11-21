package com.iprism.ecmhealthadvisor.activities

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.exoplayer2.DefaultLoadControl
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.LoadControl
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.PlaybackException
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import com.iprism.ecmhealthadvisor.databinding.ActivityVideoPlayBinding

class VideoPlayActivity : AppCompatActivity() {

    private lateinit var binding: ActivityVideoPlayBinding
    private var player: ExoPlayer? = null
    private var videoUrl = ""
    private var playbackPosition: Long = 0
    private var currentWindow: Int = 0
    private var playWhenReady: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityVideoPlayBinding.inflate(layoutInflater)
        setContentView(binding.root)
        videoUrl = intent.getStringExtra("videoUrl").toString()
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        if (videoUrl.isNullOrEmpty()) {
            showToast("Video URL is missing")
            return
        }

        if (savedInstanceState != null) {
            playbackPosition = savedInstanceState.getLong("playback_position", 0)
            currentWindow = savedInstanceState.getInt("current_window", 0)
            playWhenReady = savedInstanceState.getBoolean("play_when_ready", true)
        }
        binding.backButton.setOnClickListener {
            finish()
        }
        binding.playerView.setControllerVisibilityListener { visibility ->
            if (visibility == View.VISIBLE) {
                binding.backButton.visibility = View.VISIBLE
            } else {
                binding.backButton.visibility = View.GONE
            }
        }

        initializePlayer()

    }

    private fun initializePlayer() {
        val loadControl: LoadControl = DefaultLoadControl.Builder()
            .setBufferDurationsMs(50000, 100000, 1500, 3000)
            .build()

        val trackSelector = DefaultTrackSelector(this).apply {
            setParameters(buildUponParameters().setMaxVideoSizeSd())
        }

        player = ExoPlayer.Builder(this)
            .setTrackSelector(trackSelector)
            .setLoadControl(loadControl)
            .build().also { exoPlayer ->
                binding.playerView.player = exoPlayer

                val mediaItem = MediaItem.fromUri(Uri.parse(videoUrl))
                val mediaSource = buildMediaSource(mediaItem)
                exoPlayer.setMediaSource(mediaSource)
                exoPlayer.prepare()

                exoPlayer.seekTo(currentWindow, playbackPosition)
                exoPlayer.playWhenReady = playWhenReady

                binding.progress.visibility = View.VISIBLE

                exoPlayer.addListener(object : Player.Listener {
                    override fun onPlaybackStateChanged(state: Int) {
                        when (state) {
                            Player.STATE_BUFFERING -> binding.progress.visibility = View.VISIBLE
                            Player.STATE_READY -> binding.progress.visibility = View.GONE
                            Player.STATE_ENDED -> binding.progress.visibility = View.GONE
                            Player.STATE_IDLE -> binding.progress.visibility = View.GONE
                        }
                    }

                    override fun onPlayerError(error: PlaybackException) {
                        showToast("Error playing video: ${error.message}")
                        binding.progress.visibility = View.GONE
                    }
                })
            }
    }

    private fun buildMediaSource(mediaItem: MediaItem): MediaSource {
        val uri = mediaItem.localConfiguration?.uri ?: Uri.EMPTY
        val dataSourceFactory = DefaultHttpDataSource.Factory()

        return if (uri.toString().endsWith(".m3u8")) {
            HlsMediaSource.Factory(dataSourceFactory).createMediaSource(mediaItem)
        } else {
            ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(mediaItem)
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        player?.let {
            outState.putLong("playback_position", it.currentPosition)
            outState.putInt("current_window", it.currentWindowIndex)
            outState.putBoolean("play_when_ready", it.playWhenReady)
        }
    }

    override fun onStop() {
        super.onStop()
        player?.let {
            playbackPosition = it.currentPosition
            currentWindow = it.currentWindowIndex
            playWhenReady = it.playWhenReady
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        player?.release()
        player = null
    }


}