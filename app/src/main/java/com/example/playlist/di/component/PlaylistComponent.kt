package com.example.playlist.di.component

import com.example.playlist.detail.DetailActivity
import com.example.playlist.di.module.PlaylistModule
import com.example.playlist.di.module.RoomModule
import com.example.playlist.di.module.ViewModelModule
import com.example.playlist.main.MainActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [(PlaylistModule::class), (RoomModule::class), (ViewModelModule::class)])
interface PlaylistComponent {
    fun injectMain(activity: MainActivity)
    fun injectDetail(activity: DetailActivity)
}