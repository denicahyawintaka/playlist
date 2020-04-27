package com.example.playlist.detail

import io.reactivex.Observable
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import org.junit.Before
import org.junit.Test
import org.mockito.MockitoAnnotations

class DetailViewModelTest {

    private lateinit var mainViewModel: DetailViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }
        RxJavaPlugins.setComputationSchedulerHandler { Schedulers.trampoline() }
        RxJavaPlugins.setNewThreadSchedulerHandler { Schedulers.trampoline() }

        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }
        RxAndroidPlugins.setMainThreadSchedulerHandler { Schedulers.trampoline() }

        mainViewModel = DetailViewModel()
    }

    @Test
    fun `LoadPlaylistIntent should passing intent value to result when success`() {

        val expectedPlayWhenReady: Boolean = false
        val expectedStarWindow: Int = 3
        val expectedStarPosition: Long = 3

        val testObserver = mainViewModel.getStates().test()

        mainViewModel.processIntent(Observable.just(DetailIntent.UpdateStartPositionIntent(expectedPlayWhenReady, expectedStarWindow, expectedStarPosition )))

        testObserver.assertValueAt(1) { state ->
            !state.isError && state.playWhenReady == expectedPlayWhenReady && state.startWindow == expectedStarWindow && state.startPosition == expectedStarPosition
        }
    }
}