package com.example.playlist.detail

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.example.playlist.R
import com.example.playlist.di.component.DaggerPlaylistComponent
import com.example.playlist.di.module.RoomModule
import com.example.playlist.main.MainViewEffect
import com.example.quipper.model.entity.Course
import com.google.android.exoplayer2.DefaultRenderersFactory
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider
import com.uber.autodispose.autoDisposable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.activity_detail.*
import javax.inject.Inject

class DetailActivity : AppCompatActivity(){

    private lateinit var simpleExoPlayer: SimpleExoPlayer

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val scopeProvider = AndroidLifecycleScopeProvider.from(this)

    private lateinit var viewModel: DetailViewModel

    private lateinit var course: Course

    private val intentSubject : PublishSubject<DetailIntent> = PublishSubject.create()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DaggerPlaylistComponent.builder().roomModule(
            RoomModule(application)
        ).build().injectDetail(this)
        setContentView(R.layout.activity_detail)
        course = intent.getParcelableExtra<Course>("MainActivity")
        viewModel = ViewModelProviders.of(this, viewModelFactory)[DetailViewModel::class.java]
        viewModel.processIntent(intentSubject)
    }


    private fun initializePlayer(playWhenReady: Boolean, startWindow: Int, startPosition: Long) {
        simpleExoPlayer = SimpleExoPlayer.Builder(this, DefaultRenderersFactory(this))
            .setTrackSelector(DefaultTrackSelector())
            .build()
        simpleExoPlayer.playWhenReady = playWhenReady
        video_view.player = simpleExoPlayer
        video_view.requestFocus()
        simpleExoPlayer.seekTo(startWindow, startPosition)
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
        viewModel.getStates()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .autoDisposable(scopeProvider)
            .subscribe({ state ->
                render(state)
            },{
                Log.e(">>> Error", "error loading activity view state", it)
            })

        viewModel.getViewEffect()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .autoDisposable(scopeProvider)
            .subscribe({ viewEffect ->
                renderViewEffect(viewEffect)
            },{
                Log.e(">>> Error", "error loading activity view state", it)
            })
    }

    private fun renderViewEffect(viewEffect: DetailViewEffect) {
        when(viewEffect) {
            DetailViewEffect.ShowToastError -> {
                Toast.makeText(this, "Error", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun render(state: DetailState) {
        if(state.isError){
            //
        }else {
            Log.d(">>> Error", "error loading activity view state")
            initializePlayer(state.playWhenReady, state.startWindow, state.startPosition)
        }
    }
    private fun releasePlayer() {
        intentSubject.onNext(DetailIntent.UpdateStartPositionIntent(false, simpleExoPlayer.currentWindowIndex, simpleExoPlayer.contentPosition))
        simpleExoPlayer.release()
    }

    public override fun onStop() {
        super.onStop()
        releasePlayer()
    }
}
