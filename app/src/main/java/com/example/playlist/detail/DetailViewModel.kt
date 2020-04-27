package com.example.playlist.detail

import androidx.lifecycle.ViewModel
import javax.inject.Inject

class DetailViewModel @Inject constructor() : ViewModel(){
    var playWhenReady: Boolean = true
    var startWindow = 0
    var startPosition: Long = 0

    fun updateStartPosition(playWhenReady: Boolean, startWindow:Int,startPosition:Long){
        this.playWhenReady = playWhenReady
        this.startWindow = startWindow
        this.startPosition = startPosition
    }
}