package com.example.playlist.detail

import com.example.playlist.domain.UpdateStartPosition
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Observable
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class DetailViewModelTest {

    private lateinit var mainViewModel: DetailViewModel

    private lateinit var intentSubject: PublishSubject<DetailIntent>

    @Mock
    lateinit var mockUpdateStartPosition: UpdateStartPosition

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        mainViewModel = DetailViewModel(mockUpdateStartPosition)
    }

    @Test
    fun `UpdateStartPositionIntent should passing intent value to result when success`() {
        val expectedPlayWhenReady: Boolean = false
        val expectedStarWindow: Int = 3
        val expectedStarPosition: Long = 3

        val testObserver = mainViewModel.getStates().test()

        whenever(mockUpdateStartPosition.execute(any(), any(), any())).thenReturn(
            Observable.just(DetailResult.UpdateStartPositionResult.Success(expectedPlayWhenReady, expectedStarWindow, expectedStarPosition))
        )
        mainViewModel.processIntent(Observable.just(DetailIntent.UpdateStartPositionIntent(expectedPlayWhenReady, expectedStarWindow, expectedStarPosition )))

        testObserver.assertValueAt(1) { state ->
            !state.isError && state.playWhenReady == expectedPlayWhenReady && state.startWindow == expectedStarWindow && state.startPosition == expectedStarPosition
        }
    }

    @Test
    fun `UpdateStartPositionIntent should passing intent value to result then show ViewEffect ShowToast when Failed`() {
        val expectedError = Throwable("ERROR")
        mainViewModel.processIntent(Observable.just(DetailIntent.UpdateStartPositionIntent(false,0,0)))
        whenever(mockUpdateStartPosition.execute(any(), any(), any())).thenReturn(
            Observable.error(expectedError)
        )
        val testObserver = mainViewModel.getStates().test()
        testObserver.assertValueAt(0) { state ->
            state.isError
        }
    }


}