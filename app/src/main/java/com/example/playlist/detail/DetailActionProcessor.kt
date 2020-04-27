package com.example.playlist.detail


import com.example.playlist.domain.UpdateStartPosition
import io.reactivex.Observable
import io.reactivex.ObservableTransformer


class DetailActionProcessor(private val updateStartPosition: UpdateStartPosition) {
    private val updateStartPositionActionProcessor =
        ObservableTransformer<DetailAction.UpdateStartPositionAction, DetailResult.UpdateStartPositionResult> { actions ->
            actions.flatMap {
                updateStartPosition.execute(
                    it.playWhenReady,
                    it.startWindow,
                    it.startPosition
                )
            }.cast(DetailResult.UpdateStartPositionResult::class.java)
                .onErrorReturn(DetailResult.UpdateStartPositionResult::Failed)
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