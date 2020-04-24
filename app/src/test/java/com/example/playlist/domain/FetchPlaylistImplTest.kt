package com.example.playlist.domain

import com.example.playlist.model.repository.PlaylistRepository
import com.example.quipper.model.entity.Course
import com.nhaarman.mockitokotlin2.never
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Completable
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import net.bytebuddy.utility.RandomString
import org.junit.Before

import org.junit.Assert.*
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class FetchPlaylistImplTest {
    @Mock
    lateinit var mockedPlaylistRepository: PlaylistRepository

    lateinit var fetchPlaylist: FetchPlaylist

    @Before
    fun setUp() {
        RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }
        RxJavaPlugins.setComputationSchedulerHandler { Schedulers.trampoline() }
        RxJavaPlugins.setNewThreadSchedulerHandler { Schedulers.trampoline() }

        MockitoAnnotations.initMocks(this)
        fetchPlaylist = FetchPlaylistImpl(mockedPlaylistRepository)
    }

    @Test
    fun `execute should fetch data from repository and store to local db then emit complete when success`(){
        whenever(mockedPlaylistRepository.fetchPlaylist()).thenReturn(
            Completable.complete()
        )

        val testObserver = mockedPlaylistRepository.fetchPlaylist().test()

        testObserver.assertComplete()
    }

    @Test
    fun `execute should fetch data from repository and store to local db then emit error when failed`(){
        val expectedError = Throwable("Error")
        whenever(mockedPlaylistRepository.fetchPlaylist()).thenReturn(
            Completable.error(expectedError)
        )
        verify(mockedPlaylistRepository, never()).fetchPlaylist()

        val testObserver = mockedPlaylistRepository.fetchPlaylist().test()

        testObserver.assertError(expectedError)
    }
}