package com.example.playlist.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlist.R
import com.example.playlist.detail.DetailActivity
import com.example.playlist.di.component.DaggerPlaylistComponent
import com.example.playlist.di.module.RoomModule
import com.example.playlist.utils.MarginItemDecoration
import com.example.quipper.model.entity.Course
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider
import com.uber.autodispose.autoDisposable
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

    private val scopeProvider = AndroidLifecycleScopeProvider.from(this)

    private lateinit var mainAdapter: MainAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DaggerPlaylistComponent.builder().roomModule(
            RoomModule(application)
        ).build().injectMain(this)
        setContentView(R.layout.activity_main)

        mainAdapter = MainAdapter()
        initRecycleView()

        viewModel = ViewModelProviders.of(this, viewModelFactory)[MainViewModel::class.java]
        viewModel.processIntent(intentSubject)

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

        mainAdapter.setOnclickListener(object : MainAdapter.OnClickListener{
            override fun onClick(course: Course) {
                val intent = Intent(applicationContext, DetailActivity::class.java)
                intent.putExtra("MainActivity", course)
                startActivity(intent)
            }

        })

        swiperefresh.setOnRefreshListener {
            intentSubject.onNext(MainIntent.RefreshPlaylistIntent)
            swiperefresh.isRefreshing = false
        }
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
            progress_circular.visibility = View.INVISIBLE
            mainAdapter.setItems(state.playlist)
        } else if (state.isLoading && state.playlist.isNotEmpty() && state.isError) {
            progress_circular.visibility = View.INVISIBLE
            mainAdapter.setItems(state.playlist)
        } else if (!state.isLoading && state.playlist.isNotEmpty() && !state.isError) {
            progress_circular.visibility = View.INVISIBLE
            mainAdapter.setItems(state.playlist)
            // intentSubject.onNext(MainIntent.LoadPopularMovieIntent)
        }
    }
}
