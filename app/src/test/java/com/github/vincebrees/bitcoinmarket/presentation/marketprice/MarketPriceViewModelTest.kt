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
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Observable
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.verify
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

    private lateinit var classUnderTest : MarketPriceViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        whenever(getMarketPriceUseCase.invoke()).thenReturn(
            Observable.just(response)
        )

        classUnderTest = MarketPriceViewModel(getMarketPriceUseCase, updateMarketPriceUseCase)

        classUnderTest.liveDataCurveModel.observeForever(observerCurveModel)
        classUnderTest.liveDataMarketPriceViewState.observeForever(observerViewState)

    }

    @Test
    fun onClickedFilterSuccess(){
        Mockito.`when`(updateMarketPriceUseCase.invoke(ConstantsTest.TIMESPAN_FILTER)).thenReturn(
            Observable.just(
                DataResponse(BitcoinResponse("", "", "", "", "", arrayListOf())))
        )

        classUnderTest.onClickedFilter(ConstantsTest.TIMESPAN_FILTER)
        Mockito.verify(updateMarketPriceUseCase, times(1)).invoke(ConstantsTest.TIMESPAN_FILTER)

        val expectedStartingViewState = MarketPriceViewState(true, false, false)
        val expectedFinalViewState = MarketPriceViewState(false, false, false)
        val expectedCurveModel = CurveModel(arrayListOf(), arrayListOf())

        verify(observerViewState, times(2)).onChanged(expectedStartingViewState)
        verify(observerViewState, times(1)).onChanged(expectedFinalViewState)
        verify(observerCurveModel, times(1)).onChanged(expectedCurveModel)
    }

    @Test
    fun onClickedFilterError(){
        Mockito.`when`(updateMarketPriceUseCase.invoke(ConstantsTest.TIMESPAN_FILTER)).thenReturn(
            Observable.just(ErrorResponse()))

        classUnderTest.onClickedFilter(ConstantsTest.TIMESPAN_FILTER)
        Mockito.verify(updateMarketPriceUseCase, times(1)).invoke(ConstantsTest.TIMESPAN_FILTER)

        val expectedStartingViewState = MarketPriceViewState(true, false, false)
        val expectedFinalViewState = MarketPriceViewState(false, false, true)

        verify(observerViewState, times(2)).onChanged(expectedStartingViewState)
        verify(observerViewState, times(1)).onChanged(expectedFinalViewState)
    }
}