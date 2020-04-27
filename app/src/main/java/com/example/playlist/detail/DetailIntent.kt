package com.example.playlist.detail

sealed class DetailIntent {
    data class UpdateStartPositionIntent(val playWhenReady: Boolean, val startWindow:Int, val startPosition:Long) : DetailIntent()
}