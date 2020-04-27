package com.example.playlist.detail

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