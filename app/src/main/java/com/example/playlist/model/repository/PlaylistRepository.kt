package com.example.playlist.model.repository

import com.example.quipper.model.entity.Course
import io.reactivex.Completable
import io.reactivex.Single

interface PlaylistRepository {
    fun fetchPlaylist(): Completable
    fun getPlaylist() : Single<List<Course>>
}