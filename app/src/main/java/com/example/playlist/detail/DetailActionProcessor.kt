package com.example.playlist.detail


import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class DetailActionProcessor() {
    private val updateStartPositionActionProcessor =
        ObservableTransformer<DetailAction.UpdateStartPositionAction, DetailResult.UpdateStartPositionResult> { actions ->
            actions.flatMap {
                Observable.just(DetailResult.UpdateStartPositionResult.Success(it.playWhenReady, it.startWindow, it.startPosition))
                    .cast(DetailResult.UpdateStartPositionResult::class.java)
                    .onErrorReturn(DetailResult.UpdateStartPositionResult::Failed)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
            }
        }


    val actionProcessor = ObservableTransformer<DetailAction, DetailResult> { actions ->
        actions.publish { shared ->
            Observable.merge (
                listOf(
                    shared.ofType(DetailAction.UpdateStartPositionAction::class.java).compose(
                        updateStartPositionActionProcessor
                    )
                )
            )
                .cast(DetailResult::class.java)
                .mergeWith(
                    shared.filter {
                        it !is DetailAction.UpdateStartPositionAction
                    }.flatMap {
                        Observable.error<DetailResult>(Throwable("wrong intent"))
                    }
                )
        }
    }
}