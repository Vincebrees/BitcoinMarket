package com.github.vincebrees.bitcoinmarket.presentation.marketprice

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.github.vincebrees.bitcoinmarket.ConstantsTest
import com.github.vincebrees.bitcoinmarket.RxImmediateSchedulerRule
import com.github.vincebrees.bitcoinmarket.domain.DataResponse
import com.github.vincebrees.bitcoinmarket.domain.ErrorResponse
import com.github.vincebrees.bitcoinmarket.domain.TypeResponse
import com.github.vincebrees.bitcoinmarket.domain.entity.BitcoinResponse
import com.github.vincebrees.bitcoinmarket.domain.interactors.GetMarketPriceUseCase
import com.github.vincebrees.bitcoinmarket.domain.interactors.UpdateMarketPriceUseCase
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Observable
import okhttp3.CacheControl
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by Vincent ETIENNE on 23/02/2019.
 */
@RunWith(MockitoJUnitRunner::class)
class MarketPriceViewModelTest {
    @Rule
    @JvmField
    val rxRule = RxImmediateSchedulerRule()
    @Rule
    @JvmField
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    lateinit var getMarketPriceUseCase: GetMarketPriceUseCase
    @Mock
    lateinit var updateMarketPriceUseCase: UpdateMarketPriceUseCase
    @Mock
    lateinit var observerCurveModel: Observer<CurveModel>
    @Mock
    lateinit var observerViewState: Observer<MarketPriceViewState>
    @Mock
    lateinit var response : TypeResponse<BitcoinResponse>
    @Mock
    lateinit var dataresponse : DataResponse<BitcoinResponse>
    @Mock
    lateinit var bitcoinResponse: BitcoinResponse
    @Mock
    lateinit var errorResponse : ErrorResponse<BitcoinResponse>

    private lateinit var classUnderTest : MarketPriceViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
    }
    private fun initViewModelWithNoResponse() {
        whenever(getMarketPriceUseCase.invoke()).thenReturn(Observable.just(response))
        initViewModel()
    }

    private fun initViewModelWithDataResponse() {
        whenever(getMarketPriceUseCase.invoke()).thenReturn(Observable.just(dataresponse))
        whenever(dataresponse.data).thenReturn(bitcoinResponse)
        whenever(bitcoinResponse.values).thenReturn(arrayListOf())

        initViewModel()
    }

    private fun initViewModelWithErrorResponse() {
        whenever(getMarketPriceUseCase.invoke()).thenReturn(Observable.just(errorResponse))
        initViewModel()
    }

    private fun initViewModel() {
        classUnderTest = MarketPriceViewModel(getMarketPriceUseCase, updateMarketPriceUseCase)

        classUnderTest.liveDataCurveModel.observeForever(observerCurveModel)
        classUnderTest.liveDataMarketPriceViewState.observeForever(observerViewState)
    }


    @Test
    fun onInitShowLoading(){
        initViewModelWithNoResponse()

        verify(getMarketPriceUseCase, times(1)).invoke()

        val expectedFinalViewState = MarketPriceViewState(true, false, false)

        verify(observerViewState, times(1)).onChanged(expectedFinalViewState)
    }

    @Test
    fun onInitGetDataSuccess(){
        initViewModelWithDataResponse()

        verify(getMarketPriceUseCase, times(1)).invoke()

        val expectedFinalViewState = MarketPriceViewState(false, false, false)
        val expectedCurveModel = CurveModel(arrayListOf(), arrayListOf())

        verify(observerViewState, times(1)).onChanged(expectedFinalViewState)
        verify(observerCurveModel, times(1)).onChanged(expectedCurveModel)
    }

    @Test
    fun onInitGetDataFailed(){
        initViewModelWithErrorResponse()

        verify(getMarketPriceUseCase, times(1)).invoke()

        val expectedFinalViewState = MarketPriceViewState(false, true, false)

        verify(observerViewState, times(1)).onChanged(expectedFinalViewState)
    }

    @Test
    fun onClickedFilterSuccess(){
        initViewModelWithNoResponse()
        whenever(dataresponse.data).thenReturn(bitcoinResponse)
        whenever(bitcoinResponse.values).thenReturn(arrayListOf())

        whenever(updateMarketPriceUseCase.invoke(ConstantsTest.TIMESPAN_FILTER)).thenReturn(Observable.just(dataresponse))

        classUnderTest.onClickedFilter(ConstantsTest.TIMESPAN_FILTER)
        verify(updateMarketPriceUseCase, times(1)).invoke(ConstantsTest.TIMESPAN_FILTER)

        val expectedStartingViewState = MarketPriceViewState(true, false, false)
        val expectedFinalViewState = MarketPriceViewState(false, false, false)
        val expectedCurveModel = CurveModel(arrayListOf(), arrayListOf())

        verify(observerViewState, times(2)).onChanged(expectedStartingViewState)
        verify(observerViewState, times(1)).onChanged(expectedFinalViewState)
        verify(observerCurveModel, times(1)).onChanged(expectedCurveModel)
    }

    @Test
    fun onClickedFilterError(){
        initViewModelWithNoResponse()

        whenever(updateMarketPriceUseCase.invoke(ConstantsTest.TIMESPAN_FILTER)).thenReturn(
            Observable.just(errorResponse))

        classUnderTest.onClickedFilter(ConstantsTest.TIMESPAN_FILTER)
        verify(updateMarketPriceUseCase, times(1)).invoke(ConstantsTest.TIMESPAN_FILTER)

        val expectedStartingViewState = MarketPriceViewState(true, false, false)
        val expectedFinalViewState = MarketPriceViewState(false, false, true)
        val expectedCurveModel = CurveModel(arrayListOf(), arrayListOf())

        verify(observerViewState, times(2)).onChanged(expectedStartingViewState)
        verify(observerViewState, times(1)).onChanged(expectedFinalViewState)
        verify(observerCurveModel, times(0)).onChanged(expectedCurveModel)
    }

    @Test
    fun onRefreshSuccess(){
        initViewModelWithNoResponse()
        whenever(dataresponse.data).thenReturn(bitcoinResponse)
        whenever(bitcoinResponse.values).thenReturn(arrayListOf())

        whenever(updateMarketPriceUseCase.invoke(ConstantsTest.DEFAULT_TIMESPAN, CacheControl.FORCE_NETWORK)).thenReturn(
            Observable.just(dataresponse)
        )

        classUnderTest.onRefresh()

        verify(updateMarketPriceUseCase, times(1)).invoke(ConstantsTest.DEFAULT_TIMESPAN, CacheControl.FORCE_NETWORK)

        val expectedStartingViewState = MarketPriceViewState(true, false, false)
        val expectedFinalViewState = MarketPriceViewState(false, false, false)
        val expectedCurveModel = CurveModel(arrayListOf(), arrayListOf())

        //expectedStartingViewState shouldnt be called twice, only once on the init of the viewModel
        verify(observerViewState, times(1)).onChanged(expectedStartingViewState)
        verify(observerViewState, times(1)).onChanged(expectedFinalViewState)
        verify(observerCurveModel, times(1)).onChanged(expectedCurveModel)
    }

    @Test
    fun onRefreshError(){
        initViewModelWithNoResponse()

        whenever(updateMarketPriceUseCase.invoke(ConstantsTest.DEFAULT_TIMESPAN, CacheControl.FORCE_NETWORK)).thenReturn(
            Observable.just(errorResponse)
        )

        classUnderTest.onRefresh()

        verify(updateMarketPriceUseCase, times(1)).invoke(ConstantsTest.DEFAULT_TIMESPAN, CacheControl.FORCE_NETWORK)

        val expectedStartingViewState = MarketPriceViewState(true, false, false)
        val expectedFinalViewState = MarketPriceViewState(false, false, true)
        val expectedCurveModel = CurveModel(arrayListOf(), arrayListOf())

        //expectedStartingViewState shouldnt be called twice, only once on the init of the viewModel
        verify(observerViewState, times(1)).onChanged(expectedStartingViewState)
        verify(observerViewState, times(1)).onChanged(expectedFinalViewState)
        verify(observerCurveModel, times(0)).onChanged(expectedCurveModel)
    }

    @Test
    fun verifyOnClickedFilterSaveTimespanForRefresh(){
        //Run test filter before to init lastTimeSpan
        onClickedFilterSuccess()

        whenever(updateMarketPriceUseCase.invoke(ConstantsTest.TIMESPAN_FILTER, CacheControl.FORCE_NETWORK)).thenReturn(Observable.just(dataresponse))

        classUnderTest.onRefresh()

        verify(updateMarketPriceUseCase, times(1)).invoke(ConstantsTest.TIMESPAN_FILTER, CacheControl.FORCE_NETWORK)
    }
}