package com.example.playlist.model.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.quipper.model.entity.Course
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface CourseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrReplace(course: List<Course>): Completable

    @Query("SELECT * FROM Course")
    fun getPlaylist(): Single<List<Course>>
}