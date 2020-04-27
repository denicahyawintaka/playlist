package com.example.playlist.detail

import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.example.playlist.R
import com.example.playlist.di.component.DaggerPlaylistComponent
import com.example.playlist.di.module.RoomModule
import com.example.quipper.model.entity.Course
import com.google.android.exoplayer2.DefaultRenderersFactory
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import kotlinx.android.synthetic.main.activity_detail.*
import javax.inject.Inject

class DetailActivity : AppCompatActivity(){

    private lateinit var simpleExoPlayer: SimpleExoPlayer

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: DetailViewModel

    private lateinit var course: Course

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DaggerPlaylistComponent.builder().roomModule(
            RoomModule(application)
        ).build().injectDetail(this)
        setContentView(R.layout.activity_detail)
        course = intent.getParcelableExtra<Course>("MainActivity")
        viewModel = ViewModelProviders.of(this, viewModelFactory)[DetailViewModel::class.java]
    }

    private fun initializePlayer() {
        simpleExoPlayer = SimpleExoPlayer.Builder(this, DefaultRenderersFactory(this))
            .setTrackSelector(DefaultTrackSelector())
            .build()
        simpleExoPlayer.playWhenReady = viewModel.playWhenReady
        video_view.player = simpleExoPlayer
        video_view.requestFocus()
        simpleExoPlayer.seekTo(viewModel.startWindow, viewModel.startPosition)
        val mediaSource = buildMediaSource(Uri.parse(course.video_url))
        simpleExoPlayer.prepare(mediaSource, false, false)
    }

    private fun buildMediaSource(uri: Uri): MediaSource {
        val mediaDataSourceFactory = DefaultDataSourceFactory(this, Util.getUserAgent(this, "mediaPlayerSample"))
        return ProgressiveMediaSource.Factory(mediaDataSourceFactory)
            .createMediaSource(uri)
    }

    override fun onStart() {
        super.onStart()
        initializePlayer()
    }

    private fun releasePlayer() {
        viewModel.updateStartPosition(false, simpleExoPlayer.currentWindowIndex, simpleExoPlayer.contentPosition)
        simpleExoPlayer.release()
    }

    public override fun onStop() {
        super.onStop()
        releasePlayer()
    }
}
