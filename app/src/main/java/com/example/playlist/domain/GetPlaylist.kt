package com.example.playlist.domain

import com.example.quipper.model.entity.Course
import io.reactivex.Single

interface GetPlaylist {
    fun execute() : Single<List<Course>>
}