package com.example.playlist.main

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlist.R
import com.example.playlist.di.component.DaggerPlaylistComponent
import com.example.playlist.di.module.RoomModule
import com.example.playlist.utils.MarginItemDecoration
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: MainViewModel

    private val intentSubject : PublishSubject<MainIntent> = PublishSubject.create()

    private lateinit var mainAdapter: MainAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DaggerPlaylistComponent.builder().roomModule(
            RoomModule(application)
        ).build().inject(this)
        setContentView(R.layout.activity_main)

        mainAdapter = MainAdapter()
        initRecycleView()

        viewModel = ViewModelProviders.of(this, viewModelFactory)[MainViewModel::class.java]
        viewModel.processIntent(intentSubject)

        viewModel.getStates()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ state ->
                render(state)
            },{
                Log.e(">>> Error", "error loading activity view state", it)
            })

        viewModel.getViewEffect()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ viewEffect ->
                renderViewEffect(viewEffect)
            },{
                Log.e(">>> Error", "error loading activity view state", it)
            })
    }

    override fun onResume() {
        super.onResume()
        intentSubject.onNext(MainIntent.LoadPlaylistIntent)
    }

    private fun initRecycleView() {
        with(rv_list){
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            addItemDecoration(
                MarginItemDecoration(
                resources.getDimension(R.dimen.default_padding).toInt())
            )
            adapter = mainAdapter
        }
    }

    private fun renderViewEffect(viewEffect: MainViewEffect) {
        when(viewEffect) {
            MainViewEffect.ShowToastError -> {
                Toast.makeText(this, "Error", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun render(state: MainState) {
        if (state.isLoading && state.playlist.isEmpty() && !state.isError) {
            progress_circular.visibility = View.VISIBLE
        } else if (!state.isLoading && state.playlist.isEmpty() && state.isError) {
            //view effect toast
        } else if (!state.isLoading && state.playlist.isNotEmpty() && state.isError) {
            Log.d(">> Success", "3")
            progress_circular.visibility = View.INVISIBLE
            mainAdapter.setItems(state.playlist)
        } else if (state.isLoading && state.playlist.isNotEmpty() && state.isError) {
            Log.d(">> Success", "2")
            progress_circular.visibility = View.INVISIBLE
            mainAdapter.setItems(state.playlist)
        } else if (!state.isLoading && state.playlist.isNotEmpty() && !state.isError) {
            Log.d(">> Success", "1")
            progress_circular.visibility = View.INVISIBLE
            mainAdapter.setItems(state.playlist)
            // intentSubject.onNext(MainIntent.LoadPopularMovieIntent)
        }
    }
}
