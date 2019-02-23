package com.github.vincebrees.bitcoinmarket.presentation.marketprice

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.github.vincebrees.bitcoinmarket.RxImmediateSchedulerRule
import com.github.vincebrees.bitcoinmarket.domain.DataResponse
import com.github.vincebrees.bitcoinmarket.domain.ErrorResponse
import com.github.vincebrees.bitcoinmarket.domain.entity.BitcoinResponse
import com.github.vincebrees.bitcoinmarket.domain.entity.Coordonate
import com.github.vincebrees.bitcoinmarket.domain.interactors.GetMarketPriceUseCase
import com.github.vincebrees.bitcoinmarket.domain.interactors.UpdateMarketPriceUseCase
import com.nhaarman.mockitokotlin2.mock
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

    @Mock lateinit var observerCurveModel: Observer<CurveModel>
    @Mock lateinit var observerViewState: Observer<MarketPriceViewState>

    private lateinit var viewModel : MarketPriceViewModel

    @Before
    fun setUp() {
        getMarketPriceUseCase = mock()
        updateMarketPriceUseCase = mock()

        whenever(getMarketPriceUseCase.invoke()).thenReturn(
            Observable.just(DataResponse(BitcoinResponse("", "", "", "", "", arrayListOf())))
        )

        viewModel = MarketPriceViewModel(getMarketPriceUseCase, updateMarketPriceUseCase)

        viewModel.liveDataCurveModel.observeForever(observerCurveModel)
        viewModel.liveDataMarketPriceViewState.observeForever(observerViewState)

    }

    @Test
    fun onClickedFilterSuccess(){
        Mockito.`when`(updateMarketPriceUseCase.invoke(TIMESPAN_FILTER)).thenReturn(
            Observable.just(
                DataResponse(BitcoinResponse("", "", "", "", "", arrayListOf())))
        )

        viewModel.onClickedFilter(TIMESPAN_FILTER)
        Mockito.verify(updateMarketPriceUseCase, times(1)).invoke(TIMESPAN_FILTER)

        val expectedFinalViewState = MarketPriceViewState(false, false, false)
        val expectedCurveModel = CurveModel(arrayListOf(), arrayListOf())

        verify(observerViewState, times(2)).onChanged(expectedFinalViewState)
        verify(observerCurveModel, times(2)).onChanged(expectedCurveModel)
    }

    @Test
    fun onClickedFilterError(){
        Mockito.`when`(updateMarketPriceUseCase.invoke(TIMESPAN_FILTER)).thenReturn(
            Observable.just(ErrorResponse()))

        viewModel.onClickedFilter(TIMESPAN_FILTER)
        Mockito.verify(updateMarketPriceUseCase, times(1)).invoke(TIMESPAN_FILTER)

        val expectedFinalViewState = MarketPriceViewState(false, false, true)
        verify(observerViewState, times(1)).onChanged(expectedFinalViewState)
    }

    companion object {
        const val TIMESPAN_FILTER = "60days"

        fun getArrayListCoordinate() : List<Coordonate>{
            val list = arrayListOf<Coordonate>()
            for (x in 1 until 20){
                list.add(Coordonate(x.toLong(),x.toDouble()))
            }
            return list
        }
    }
}