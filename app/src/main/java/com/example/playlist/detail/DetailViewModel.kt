package com.example.playlist.detail

import androidx.lifecycle.ViewModel
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class DetailViewModel @Inject constructor() : ViewModel(){

    private val intentSubject: PublishSubject<DetailIntent> = PublishSubject.create()
    private val viewEffectSubject: PublishSubject<DetailViewEffect> = PublishSubject.create()

    private val intentFilter: ObservableTransformer<DetailIntent, DetailIntent>
        get() = ObservableTransformer{ intentObservable ->
            intentObservable.publish { shared  ->
                Observable.merge(
                    listOf(
                        shared.ofType(DetailIntent.UpdateStartPositionIntent::class.java).take(1)
                    )
                )
                    .cast(DetailIntent::class.java)
                    .mergeWith(
                        shared.filter {
                            it is DetailIntent.UpdateStartPositionIntent
                        }
                    )
            }
        }

    private val actionProcessor = DetailActionProcessor()

    private val stateObservable: Observable<DetailState> = compose()

    fun processIntent(intentObservable: Observable<DetailIntent>) {
        intentObservable.subscribe(intentSubject)
    }

    fun getStates(): Observable<DetailState> {
        return stateObservable
    }

    fun getViewEffect(): Observable<DetailViewEffect> {
        return viewEffectSubject
    }

    private fun intentToAction(intent: DetailIntent): DetailAction {
        return when (intent) {
            is DetailIntent.UpdateStartPositionIntent -> {
                DetailAction.UpdateStartPositionAction(intent.playWhenReady, intent.startWindow, intent.startPosition)
            }
        }
    }

    private fun reducer(previousState: DetailState, result: DetailResult): DetailState{
        return when (result) {
            is DetailResult.UpdateStartPositionResult.Success -> {
                previousState.copy(isError = false, playWhenReady = result.playWhenReady, startWindow = result.startWindow, startPosition = result.startPosition)
            }
            is DetailResult.UpdateStartPositionResult.Failed -> {
                previousState.copy(isError = true)
            }
        }
    }

    private fun handleEffect(result: DetailResult) {
        if (result is DetailResult.UpdateStartPositionResult.Failed) {
            viewEffectSubject.onNext(DetailViewEffect.ShowToastError)
        }
    }

    private fun compose(): Observable<DetailState> {
        return intentSubject
            .compose(intentFilter)
            .map(this::intentToAction)
            .compose(actionProcessor.actionProcessor)
            .doOnNext(this::handleEffect)
            .scan(DetailState.default(), this::reducer)
            .distinctUntilChanged()
            .replay(1)
            .autoConnect(0)
    }
}