package com.github.vincebrees.bitcoinmarket.data.remote

import com.github.vincebrees.bitcoinmarket.ConstantsTest
import com.github.vincebrees.bitcoinmarket.RxImmediateSchedulerRule
import com.github.vincebrees.bitcoinmarket.data.remote.pojo.RestBitcoinResponse
import com.github.vincebrees.bitcoinmarket.data.remote.pojo.toEntity
import com.github.vincebrees.bitcoinmarket.domain.DataResponse
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Observable
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Response


/**
 * Created by Vincent ETIENNE on 23/02/2019.
 */
@RunWith(MockitoJUnitRunner::class)
class RemoteDataSourceTest {
    @Rule
    @JvmField
    val rxRule = RxImmediateSchedulerRule()

    @Mock
    lateinit var bitcoinService: BitcoinService

    @Mock
    lateinit var response : Response<RestBitcoinResponse>

    var mockData = RestBitcoinResponse("", "", "", "", "", arrayListOf())


    private lateinit var classUnderTest : RemoteDataSource

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        classUnderTest = RemoteDataSource(bitcoinService)

        whenever(bitcoinService.getMarketPrice(Mockito.anyString(), Mockito.any())).thenReturn(Observable.just(response))
        whenever(response.body()).thenReturn(mockData)
    }

    @Test
    fun getMarketPriceOnResponseSuccess(){
        whenever(response.isSuccessful).thenReturn(true)

        val result = classUnderTest.getMarketPrice(ConstantsTest.TIMESPAN_FILTER, null)

        result.test()
            .assertComplete()
            .assertValue(DataResponse(mockData.toEntity()))
    }

    @Test
    fun getMarketPriceOnResponseError(){
        whenever(response.isSuccessful).thenReturn(false)

        val result = classUnderTest.getMarketPrice(ConstantsTest.TIMESPAN_FILTER, null)

        result.test().assertComplete()
        //Cannot assert that value is type ErrorResponse. :/
    }
}