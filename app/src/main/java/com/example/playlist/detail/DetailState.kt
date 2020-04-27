package com.example.playlist.detail

import com.example.quipper.model.entity.Course

data class DetailState(
    val isError: Boolean = false,
    val playWhenReady: Boolean = true,
    val startWindow:Int = 0,
    val startPosition:Long = 0
){
    companion object{
        fun default(): DetailState{
            return DetailState()
        }
    }
}