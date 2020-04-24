package com.example.playlist.di.component

import com.example.playlist.di.module.RoomModule
import com.example.playlist.main.MainActivity
import com.example.playlist.di.module.PlaylistModule
import com.example.playlist.di.module.ViewModelModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [(PlaylistModule::class), (RoomModule::class), (ViewModelModule::class)])
interface PlaylistComponent {
    fun inject(activity: MainActivity)
}