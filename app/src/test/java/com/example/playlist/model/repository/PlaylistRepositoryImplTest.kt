package com.example.playlist.model.repository

import com.example.playlist.model.dao.CourseDao
import com.example.quipper.model.entity.Course
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.never
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.quipper.book.network.ApiService
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import net.bytebuddy.utility.RandomString
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class PlaylistRepositoryImplTest{
    private lateinit var playlistRepository: PlaylistRepository

    @Mock
    private lateinit var mockedApiService: ApiService

    @Mock
    private lateinit var mockedCourseDao: CourseDao

    @Before
    fun setUP(){
        MockitoAnnotations.initMocks(this)
        playlistRepository = PlaylistRepositoryImpl(mockedApiService, mockedCourseDao)
    }

    @Test
    fun `fetchPlaylist should make network service fetch Playlist then store it to room then emit Complete`(){
        val course = Course(
            title = RandomString.make(2),
            description = RandomString.make(2),
            presenter_name = RandomString.make(2),
            thumbnail_url = RandomString.make(2),
            video_url = RandomString.make(2),
            video_duration = 20
        )

        val expectedResult = listOf(course)


        whenever(mockedApiService.fetchPlaylist()).thenReturn(
            Single.just(expectedResult)
        )

        whenever(mockedCourseDao.insertOrReplace(expectedResult)).thenReturn(
            Completable.complete()
        )

        val testObserver = playlistRepository.fetchPlaylist().test()

        testObserver.assertComplete()
    }

    @Test
    fun `fetchPlaylist should make network service fetch Playlist and when it emit error then emit error`(){
        val expectedError = Throwable("error")


        whenever(mockedApiService.fetchPlaylist()).thenReturn(
            Single.error(expectedError)
        )

        verify(mockedCourseDao, never()).insertOrReplace(any())

        val testObserver = playlistRepository.fetchPlaylist().test()
        testObserver.assertError(expectedError)
    }

    @Test
    fun `fetchPlaylist should make network service fetch Playlist then store it to room but when the store is failed it will emit error`(){
        val course = Course(
            title = RandomString.make(2),
            description = RandomString.make(2),
            presenter_name = RandomString.make(2),
            thumbnail_url = RandomString.make(2),
            video_url = RandomString.make(2),
            video_duration = 20
        )
        val expectedResult = listOf(course)

        val expectedError = Throwable("error")

        whenever(mockedApiService.fetchPlaylist()).thenReturn(
            Single.error(expectedError)
        )

        whenever(mockedCourseDao.insertOrReplace(expectedResult)).thenReturn(
            Completable.error(expectedError)
        )

        val testObserver = playlistRepository.fetchPlaylist().test()
        testObserver.assertError(expectedError)
    }

    @Test
    fun `getPlaylist should make dao get data from room then emit playlist when success`(){
        val course = Course(
            title = RandomString.make(2),
            description = RandomString.make(2),
            presenter_name = RandomString.make(2),
            thumbnail_url = RandomString.make(2),
            video_url = RandomString.make(2),
            video_duration = 20
        )

        val expectedResult = listOf(course)

        whenever(mockedCourseDao.getPlaylist()).thenReturn(
            Single.just(expectedResult)
        )

        val testObserver = playlistRepository.getPlaylist().test()
        testObserver.assertValueAt(0){playlist ->
            playlist == expectedResult
        }
    }

    @Test
    fun `getPlaylist should make dao get data from room then emit error when failed`(){
        val expectedError = Throwable("Error")

        whenever(mockedCourseDao.getPlaylist()).thenReturn(
            Single.error(expectedError)
        )

        val testObserver = playlistRepository.getPlaylist().test()
        testObserver.assertError(expectedError)
    }

}