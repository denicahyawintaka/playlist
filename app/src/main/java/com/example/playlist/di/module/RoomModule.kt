package com.example.playlist.di.module

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.playlist.model.dao.CourseDao
import com.example.playlist.model.database.AppDatabase
import com.quipper.book.network.ApiService
import com.quipper.book.network.RetrofitClient
import dagger.Module
import dagger.Provides
import java.security.AccessControlContext
import javax.inject.Singleton

@Module
class RoomModule(private val application: Application) {

    @Provides
    fun providesAppContext() = application

    @Singleton
    @Provides
    fun provideAppDatabase(application: Application): AppDatabase {
        return Room
            .databaseBuilder(application, AppDatabase::class.java, "Playlist-DB")
            .fallbackToDestructiveMigration()
            .allowMainThreadQueries()
            .build()
    }

    @Provides
    fun provideCourseDao(appDataBase: AppDatabase): CourseDao {
        return appDataBase.courseDao()
    }
}