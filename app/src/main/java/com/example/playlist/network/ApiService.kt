package com.quipper.book.network

import com.example.quipper.model.entity.Course
import io.reactivex.Single
import retrofit2.http.GET

interface ApiService{
    @GET("native-technical-exam/playlist.json")
    fun fetchPlaylist(): Single<List<Course>>
}
