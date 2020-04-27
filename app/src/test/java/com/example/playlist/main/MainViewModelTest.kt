package com.example.playlist.main

import com.example.playlist.domain.FetchPlaylist
import com.example.playlist.domain.GetPlaylist
import com.example.quipper.model.entity.Course
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import net.bytebuddy.utility.RandomString
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class MainViewModelTest {
    private lateinit var mainViewModel: MainViewModel

    @Mock
    private lateinit var mockedFetchPlaylistUseCase: FetchPlaylist

    @Mock
    private lateinit var mockedGetPlaylistUseCase: GetPlaylist

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }
        RxJavaPlugins.setComputationSchedulerHandler { Schedulers.trampoline() }
        RxJavaPlugins.setNewThreadSchedulerHandler { Schedulers.trampoline() }

        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }
        RxAndroidPlugins.setMainThreadSchedulerHandler { Schedulers.trampoline() }

        mainViewModel = MainViewModel(mockedFetchPlaylistUseCase, mockedGetPlaylistUseCase)
    }

    @Test
    fun `LoadPlaylistIntent should call FetchPlaylistUseCase then call GetPlaylistUseCase when Complete then emit result from usecase emit data`() {
        val course = Course(
            title = RandomString.make(2),
            description = RandomString.make(2),
            presenter_name = RandomString.make(2),
            thumbnail_url = RandomString.make(2),
            video_url = RandomString.make(2),
            video_duration = 20
        )
        val expectedData = listOf(course)

        val testObserver = mainViewModel.getStates().test()

        whenever(mockedFetchPlaylistUseCase.execute()).thenReturn(
            Completable.complete()
        )

        whenever(mockedGetPlaylistUseCase.execute()).thenReturn(
            Single.just(expectedData)
        )

        mainViewModel.processIntent(Observable.just(MainIntent.LoadPlaylistIntent))

        testObserver.assertValueAt(0) { state ->
            state.isLoading && !state.isError && state.playlist.isEmpty()
        }

        testObserver.assertValueAt(1) { state ->
            !state.isLoading && !state.isError && state.playlist.isNotEmpty()
        }
    }

    @Test
    fun `LoadPlaylistIntent should call FetchPlaylistUseCase then show ViewEffect ShowToast when Error`() {
        val expectedError = Throwable("ERROR")
        val course = Course(
            title = RandomString.make(2),
            description = RandomString.make(2),
            presenter_name = RandomString.make(2),
            thumbnail_url = RandomString.make(2),
            video_url = RandomString.make(2),
            video_duration = 20
        )
        val expectedData = listOf(course)

        val testObserver = mainViewModel.getStates().test()
        val testViewEffectObserver = mainViewModel.getViewEffect().test()

        whenever(mockedFetchPlaylistUseCase.execute()).thenReturn(
            Completable.error(expectedError)
        )

        whenever(mockedGetPlaylistUseCase.execute()).thenReturn(
            Single.just(expectedData)
        )

        mainViewModel.processIntent(Observable.just(MainIntent.LoadPlaylistIntent))

        testObserver.assertValueAt(0) { state ->
            state.isLoading && !state.isError && state.playlist.isEmpty()
        }

        testViewEffectObserver.assertValueAt(0) { effect ->
            effect == MainViewEffect.ShowToastError
        }

        testObserver.assertValueAt(1) { state ->
            !state.isLoading && state.isError && state.playlist.isEmpty()
        }
    }


    @Test
    fun `LoadPlaylistIntent should call FetchPlaylistUseCase then call GetPlaylistUseCase when Complete then show ViewEffect ShowToast when usecase emit Error`() {
        val expectedError = Throwable("ERROR")

        val testObserver = mainViewModel.getStates().test()
        val testViewEffectObserver = mainViewModel.getViewEffect().test()

        whenever(mockedFetchPlaylistUseCase.execute()).thenReturn(
            Completable.error(expectedError)
        )

        whenever(mockedGetPlaylistUseCase.execute()).thenReturn(
            Single.error(expectedError)
        )

        mainViewModel.processIntent(Observable.just(MainIntent.LoadPlaylistIntent))

        testObserver.assertValueAt(0) { state ->
            state.isLoading && !state.isError && state.playlist.isEmpty()
        }

        testViewEffectObserver.assertValueAt(0) { effect ->
            effect == MainViewEffect.ShowToastError
        }

        testObserver.assertValueAt(1) { state ->
            !state.isLoading && state.isError && state.playlist.isEmpty()
        }
    }

    @Test
    fun `RefreshPlaylistIntent should call FetchPlaylistUseCase then call GetPlaylistUseCase when Complete then emit result from usecase emit data`() {
        val course = Course(
            title = RandomString.make(2),
            description = RandomString.make(2),
            presenter_name = RandomString.make(2),
            thumbnail_url = RandomString.make(2),
            video_url = RandomString.make(2),
            video_duration = 20
        )
        val expectedData = listOf(course)

        val testObserver = mainViewModel.getStates().test()

        whenever(mockedFetchPlaylistUseCase.execute()).thenReturn(
            Completable.complete()
        )

        whenever(mockedGetPlaylistUseCase.execute()).thenReturn(
            Single.just(expectedData)
        )

        mainViewModel.processIntent(Observable.just(MainIntent.LoadPlaylistIntent))

        testObserver.assertValueAt(0) { state ->
            state.isLoading && !state.isError && state.playlist.isEmpty()
        }

        testObserver.assertValueAt(1) { state ->
            !state.isLoading && !state.isError && state.playlist.isNotEmpty()
        }
    }

    @Test
    fun `RefreshPlaylistIntent should call FetchPlaylistUseCase then show ViewEffect ShowToast when Error`() {
        val expectedError = Throwable("ERROR")
        val course = Course(
            title = RandomString.make(2),
            description = RandomString.make(2),
            presenter_name = RandomString.make(2),
            thumbnail_url = RandomString.make(2),
            video_url = RandomString.make(2),
            video_duration = 20
        )
        val expectedData = listOf(course)

        val testObserver = mainViewModel.getStates().test()
        val testViewEffectObserver = mainViewModel.getViewEffect().test()

        whenever(mockedFetchPlaylistUseCase.execute()).thenReturn(
            Completable.error(expectedError)
        )

        whenever(mockedGetPlaylistUseCase.execute()).thenReturn(
            Single.just(expectedData)
        )

        mainViewModel.processIntent(Observable.just(MainIntent.LoadPlaylistIntent))

        testObserver.assertValueAt(0) { state ->
            state.isLoading && !state.isError && state.playlist.isEmpty()
        }

        testViewEffectObserver.assertValueAt(0) { effect ->
            effect == MainViewEffect.ShowToastError
        }

        testObserver.assertValueAt(1) { state ->
            !state.isLoading && state.isError && state.playlist.isEmpty()
        }
    }


    @Test
    fun `RefreshPlaylistIntent should call FetchPlaylistUseCase then call GetPlaylistUseCase when Complete then show ViewEffect ShowToast when usecase emit Error`() {
        val expectedError = Throwable("ERROR")

        val testObserver = mainViewModel.getStates().test()
        val testViewEffectObserver = mainViewModel.getViewEffect().test()

        whenever(mockedFetchPlaylistUseCase.execute()).thenReturn(
            Completable.error(expectedError)
        )

        whenever(mockedGetPlaylistUseCase.execute()).thenReturn(
            Single.error(expectedError)
        )

        mainViewModel.processIntent(Observable.just(MainIntent.LoadPlaylistIntent))

        testObserver.assertValueAt(0) { state ->
            state.isLoading && !state.isError && state.playlist.isEmpty()
        }

        testViewEffectObserver.assertValueAt(0) { effect ->
            effect == MainViewEffect.ShowToastError
        }

        testObserver.assertValueAt(1) { state ->
            !state.isLoading && state.isError && state.playlist.isEmpty()
        }
    }
}