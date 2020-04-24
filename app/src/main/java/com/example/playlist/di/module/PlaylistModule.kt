package com.example.playlist.di.module

import com.example.playlist.domain.FetchPlaylist
import com.example.playlist.domain.FetchPlaylistImpl
import com.example.playlist.domain.GetPlaylist
import com.example.playlist.domain.GetPlaylistImpl
import com.example.playlist.model.dao.CourseDao
import com.example.playlist.model.repository.PlaylistRepository
import com.example.playlist.model.repository.PlaylistRepositoryImpl
import com.quipper.book.network.ApiService
import com.quipper.book.network.RetrofitClient
import dagger.Module
import dagger.Provides

@Module
class PlaylistModule {
    @Provides
    fun provideApiService(): ApiService {
        return RetrofitClient.apiService
    }

    @Provides
    fun providePlaylistRepository(apiService: ApiService, courseDao: CourseDao ): PlaylistRepository{
        return PlaylistRepositoryImpl(apiService, courseDao)
    }

    @Provides
    fun provideFetchPlaylist(playlistRepository: PlaylistRepository): FetchPlaylist{
        return FetchPlaylistImpl(playlistRepository)
    }

    @Provides
    fun provideGetPlaylist(playlistRepository: PlaylistRepository): GetPlaylist{
        return GetPlaylistImpl(playlistRepository)
    }
}