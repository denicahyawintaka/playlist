package com.example.playlist.model.repository

import com.example.playlist.model.dao.CourseDao
import com.example.quipper.model.entity.Course
import com.quipper.book.network.ApiService
import io.reactivex.Completable
import io.reactivex.Single

class PlaylistRepositoryImpl(private val apiService: ApiService, private val courseDao: CourseDao) : PlaylistRepository{
    override fun fetchPlaylist() :Completable{
        return apiService.fetchPlaylist().flatMapCompletable { playlist ->
            courseDao.insertOrReplace(playlist)
        }
    }

    override fun getPlaylist(): Single<List<Course>> {
        return courseDao.getPlaylist()
    }

}