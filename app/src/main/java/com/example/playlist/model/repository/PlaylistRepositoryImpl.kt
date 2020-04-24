package com.example.playlist.model.repository

import android.util.Log
import com.example.playlist.model.dao.CourseDao
import com.example.quipper.model.entity.Course
import com.quipper.book.network.ApiService
import io.reactivex.Completable
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Deferred

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