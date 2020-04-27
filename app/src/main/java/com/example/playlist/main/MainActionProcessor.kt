package com.example.playlist.main


import com.example.playlist.domain.FetchPlaylist
import com.example.playlist.domain.GetPlaylist
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class MainActionProcessor(private val fetchPlaylist: FetchPlaylist,
                          private val getPlaylist: GetPlaylist) {

    private val loadPlaylistActionProcessor =
        ObservableTransformer<MainAction.LoadPlaylistAction, MainResult.LoadPlaylistResult> { actions ->
            actions.flatMap {
                fetchPlaylist.execute()
                    .andThen(getPlaylist.execute())
                    .toObservable()
                    .map {playlist ->
                        MainResult.LoadPlaylistResult.Success(playlist)
                    }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
            }
                .cast(MainResult.LoadPlaylistResult::class.java)
                .startWith(MainResult.LoadPlaylistResult.IsLoading)
                .onErrorReturn(MainResult.LoadPlaylistResult::Failed)
        }

    private val refreshPlaylistActionProcessor =
        ObservableTransformer<MainAction.RefreshPlaylistAction, MainResult.RefreshPlaylistResult> { actions ->
            actions.flatMap {
                fetchPlaylist.execute()
                    .andThen(getPlaylist.execute())
                    .toObservable()
                    .map {playlist ->
                        MainResult.RefreshPlaylistResult.Success(playlist.reversed())
                    }
                    .cast(MainResult.RefreshPlaylistResult::class.java)
                    .startWith(MainResult.RefreshPlaylistResult.IsLoading)
                    .onErrorReturn(MainResult.RefreshPlaylistResult::Failed)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
            }
        }

    val actionProcessor = ObservableTransformer<MainAction, MainResult> { actions ->
        actions.publish { shared ->
            Observable.merge (
                listOf(
                    shared.ofType(MainAction.LoadPlaylistAction::class.java).compose(
                        loadPlaylistActionProcessor
                    ),

                    shared.ofType(MainAction.RefreshPlaylistAction::class.java).compose(
                        refreshPlaylistActionProcessor
                    )

                )
            )
                .cast(MainResult::class.java)
                .mergeWith(
                    shared.filter {
                        it !is MainAction.LoadPlaylistAction && it !is MainAction.RefreshPlaylistAction

                    }.flatMap {
                        Observable.error<MainResult>(Throwable("wrong intent"))
                    }
                )
        }
    }
}