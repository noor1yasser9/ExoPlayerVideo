package com.nurbk.ps.exoplayervideo

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.android.exoplayer2.ExoPlaybackException
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.source.dash.DashMediaSource
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var simpleExoplayer: SimpleExoPlayer? = null
    private var playbackPosition: Long = 0
    private val mp4Url1 = "https://html5demos.com/assets/dizzy.mp4"
    private val mp4Url2 = "https://html5demos.com/assets/remy-and-ellis2.mp4"
    private val mp4Url3 = "https://www.w3schools.com/html/mov_bbb.mp4"
    private val dashUrl = "https://storage.googleapis.com/wvmedia/clear/vp9/tears/tears_uhd.mpd"
    private val DEFAULT = "default"
    private val DASH = "dash"

    private val dataSourceFactory: DataSource.Factory by lazy {
        DefaultDataSourceFactory(this, "exoplayer-sample")
    }

    var videoUrl: String? = null
    var videoType: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button1.setOnClickListener {
            videoUrl = mp4Url1
            videoType = DEFAULT
            initializePlayer(mp4Url1, DEFAULT)
        }
        button2.setOnClickListener {
            videoUrl = dashUrl
            videoType = DASH
            initializePlayer(dashUrl, DASH)
        }
        button3.setOnClickListener {
            videoUrl = mp4Url2
            videoType = DEFAULT
            initializePlayer(mp4Url2, DEFAULT)
        }
        button4.setOnClickListener {
            videoUrl = mp4Url3
            videoType = DEFAULT
            initializePlayer(mp4Url3, DEFAULT)
        }
    }


    override fun onStart() {
        super.onStart()

    }

    override fun onResume() {
        super.onResume()
        if (videoUrl != null)
            initializePlayer(videoUrl!!, videoType!!)
    }

    override fun onStop() {
        super.onStop()
        releasePlayer()
    }

    private fun initializePlayer(uri: String, type: String) {
        simpleExoplayer = SimpleExoPlayer.Builder(this).build()
        preparePlayer(uri, type)
        videoPlayer.player = simpleExoplayer
        simpleExoplayer?.seekTo(playbackPosition)
        simpleExoplayer?.playWhenReady = true
    }

    private fun buildMediaSource(uri: Uri, type: String): MediaSource {
        return if (type == "dash") {
            DashMediaSource.Factory(dataSourceFactory)
                .createMediaSource(uri)
        } else {
            ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(uri)
        }
    }

    private fun preparePlayer(videoUrl: String, type: String) {
        val uri = Uri.parse(videoUrl)
        val mediaSource = buildMediaSource(uri, type)
        simpleExoplayer?.prepare(mediaSource)
    }

    private fun releasePlayer() {
        playbackPosition = simpleExoplayer?.currentPosition ?: 0
        simpleExoplayer?.release()
    }


}