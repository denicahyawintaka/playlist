package com.example.playlist.domain

import com.example.playlist.model.repository.PlaylistRepository
import com.example.quipper.model.entity.Course
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Single
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import net.bytebuddy.utility.RandomString
import org.junit.Before
import org.junit.Test
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

    @Test
    fun `execute should load local db then emit result when success`(){
        val course1 = Course(
            title = RandomString.make(2),
            description = RandomString.make(2),
            presenter_name = RandomString.make(2),
            thumbnail_url = RandomString.make(2),
            video_url = RandomString.make(2),
            video_duration = 20
        )

        val course2 = Course(
            title = RandomString.make(2),
            description = RandomString.make(2),
            presenter_name = RandomString.make(2),
            thumbnail_url = RandomString.make(2),
            video_url = RandomString.make(2),
            video_duration = 20
        )

        val expectedResult = listOf(course1, course2)

        whenever(mockedPlaylistRepository.getPlaylist()).thenReturn(
            Single.just(expectedResult)
        )

        val testObserver = mockedPlaylistRepository.getPlaylist().test()

        testObserver.assertValue(expectedResult)
    }

    @Test
    fun `execute should load local db then emit error when failed`(){
        val expectedError = Throwable("Error")

        whenever(mockedPlaylistRepository.getPlaylist()).thenReturn(
            Single.error(expectedError)
        )

        val testObserver = mockedPlaylistRepository.getPlaylist().test()
        testObserver.assertError(expectedError)
    }
}