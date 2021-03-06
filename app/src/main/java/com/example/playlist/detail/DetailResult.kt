package com.example.playlist.detail

sealed class DetailResult {

    sealed class UpdateStartPositionResult: DetailResult(){
        data class Success(val playWhenReady: Boolean, val startWindow:Int, val startPosition:Long) : UpdateStartPositionResult()
        data class Failed(val error: Throwable) : UpdateStartPositionResult()
    }
}