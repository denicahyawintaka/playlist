package com.example.playlist.main

import com.example.quipper.model.entity.Course

sealed class MainResult {
    sealed class LoadPlaylistResult: MainResult(){
        object IsLoading : LoadPlaylistResult()
        data class Success(val playlist: List<Course>) : LoadPlaylistResult()
        data class Failed(val error: Throwable) : LoadPlaylistResult()
    }
}