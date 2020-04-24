package com.example.playlist.main

import com.example.quipper.model.entity.Course

data class MainState(
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val playlist: List<Course> = emptyList()
){
    companion object{
        fun default(): MainState{
            return MainState()
        }
    }
}