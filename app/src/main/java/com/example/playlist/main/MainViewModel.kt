package com.example.playlist.main

import androidx.lifecycle.ViewModel
import com.example.playlist.domain.FetchPlaylist
import com.example.playlist.domain.GetPlaylist
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class MainViewModel @Inject constructor(fetchPlaylist: FetchPlaylist, getPlaylist: GetPlaylist) : ViewModel() {

    private val intentSubject: PublishSubject<MainIntent> = PublishSubject.create()
    private val viewEffectSubject: PublishSubject<MainViewEffect> = PublishSubject.create()

    private val intentFilter: ObservableTransformer<MainIntent, MainIntent>
        get() = ObservableTransformer{ intentObservable ->
            intentObservable.publish { shared  ->
                Observable.merge(
                    listOf(
                        shared.ofType(MainIntent.LoadPlaylistIntent::class.java).take(1),
                        shared.ofType(MainIntent.RefreshPlaylistIntent::class.java).take(1)
                    )
                )
                    .cast(MainIntent::class.java)
                    .mergeWith(
                        shared.filter {
                            it is MainIntent.LoadPlaylistIntent || it is MainIntent.RefreshPlaylistIntent
                        }
                    )
            }
        }

    private val actionProcessor = MainActionProcessor(fetchPlaylist, getPlaylist)

    private val stateObservable: Observable<MainState> = compose()

    fun processIntent(intentObservable: Observable<MainIntent>) {
        intentObservable.subscribe(intentSubject)
    }

    fun getStates(): Observable<MainState> {
        return stateObservable
    }

    fun getViewEffect(): Observable<MainViewEffect> {
        return viewEffectSubject
    }

    private fun intentToAction(intent: MainIntent): MainAction{
        return when (intent) {
            is MainIntent.LoadPlaylistIntent -> {
                MainAction.LoadPlaylistAction
            }
            is MainIntent.RefreshPlaylistIntent -> {
                MainAction.RefreshPlaylistAction
            }
        }
    }

    private fun reducer(previousState: MainState, result: MainResult): MainState{
        return when (result){
            is MainResult.LoadPlaylistResult.IsLoading -> {
                previousState.copy(isLoading = true)
            }
            is MainResult.LoadPlaylistResult.Success -> {
                previousState.copy(isLoading = false, playlist = result.playlist)
            }
            is MainResult.LoadPlaylistResult.Failed -> {
                previousState.copy(isLoading = false, isError = true)
            }
            MainResult.RefreshPlaylistResult.IsLoading -> {
                previousState.copy(isLoading = true)
            }
            is MainResult.RefreshPlaylistResult.Success -> {
                previousState.copy(isLoading = false, playlist = result.playlist)
            }
            is MainResult.RefreshPlaylistResult.Failed -> {
                previousState.copy(isLoading = false, isError = true)
            }
        }
    }

    private fun handleEffect(result: MainResult) {
        if (result is MainResult.LoadPlaylistResult.Failed) {
            viewEffectSubject.onNext(MainViewEffect.ShowToastError)
        }
    }

    private fun compose(): Observable<MainState> {
        return intentSubject
            .compose(intentFilter)
            .map(this::intentToAction)
            .compose(actionProcessor.actionProcessor)
            .doOnNext(this::handleEffect)
            .scan(MainState.default(), this::reducer)
            .distinctUntilChanged()
            .replay(1)
            .autoConnect(0)
    }
}