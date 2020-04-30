package com.example.playlist.detail

sealed class DetailAction {
    data class UpdateStartPositionAction(val playWhenReady: Boolean, val startWindow:Int, val startPosition:Long) : DetailAction()

}