package com.example.playlist.model.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.playlist.model.dao.CourseDao
import com.example.quipper.model.entity.Course

@Database(
    version = 14, entities = [
        Course::class
    ]
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun courseDao(): CourseDao
}