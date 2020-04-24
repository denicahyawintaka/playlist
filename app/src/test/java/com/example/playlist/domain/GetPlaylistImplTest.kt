package com.example.playlist.domain

import com.example.playlist.model.repository.PlaylistRepository
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import org.junit.Before

import org.junit.Assert.*
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class GetPlaylistImplTest {
    @Mock
    lateinit var mockedPlaylistRepository: PlaylistRepository

    lateinit var getPlaylist: GetPlaylist

    @Before
    fun setUp() {
        RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }
        RxJavaPlugins.setComputationSchedulerHandler { Schedulers.trampoline() }
        RxJavaPlugins.setNewThreadSchedulerHandler { Schedulers.trampoline() }

        MockitoAnnotations.initMocks(this)
        getPlaylist = GetPlaylistImpl(mockedPlaylistRepository)
    }
}