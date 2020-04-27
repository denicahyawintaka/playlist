package com.example.playlist.domain

import com.example.playlist.detail.DetailResult
import io.reactivex.Observable

interface UpdateStartPosition {
    fun execute(isPlayWhenReady:Boolean,windowPosition:Int,startPosition:Long): Observable<DetailResult>
}