package com.example.playlist.domain

import com.example.playlist.detail.DetailResult
import io.reactivex.Observable

class UpdateStartPositionImpl : UpdateStartPosition {
    override fun execute(isPlayWhenReady: Boolean, windowPosition: Int, startPosition: Long): Observable<DetailResult> {
        return Observable.just(DetailResult.UpdateStartPositionResult.Success(isPlayWhenReady, windowPosition, startPosition))
    }
}