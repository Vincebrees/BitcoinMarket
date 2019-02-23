package com.github.vincebrees.bitcoinmarket.domain.interactors

import com.github.vincebrees.bitcoinmarket.ConstantsTest
import com.github.vincebrees.bitcoinmarket.RxImmediateSchedulerRule
import com.github.vincebrees.bitcoinmarket.domain.repository.BitcoinRepository
import com.nhaarman.mockitokotlin2.verify
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
class UpdateMarketPriceUseCaseTest {
    @Rule
    @JvmField
    val rxRule = RxImmediateSchedulerRule()

    @Mock
    lateinit var bitcoinRepository: BitcoinRepository

    private lateinit var classUnderTest : UpdateMarketPriceUseCase

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        classUnderTest = UpdateMarketPriceUseCase(bitcoinRepository)
    }

    @Test
    fun testInvokeWithTimeSpan(){
        classUnderTest.invoke(ConstantsTest.TIMESPAN_FILTER)

        verify(bitcoinRepository).getMarketPrice(ConstantsTest.TIMESPAN_FILTER, null)
    }

    @Test
    fun testInvokeWithoutTimeSpan(){
        classUnderTest.invoke(null)

        verify(bitcoinRepository).getMarketPrice(null, null)
    }

    @Test
    fun testInvokeWithCacheControl(){
        classUnderTest.invoke(null, CacheControl.FORCE_NETWORK)

        verify(bitcoinRepository).getMarketPrice(null, CacheControl.FORCE_NETWORK)
    }

    @Test
    fun testInvokeWithoutCacheControl(){
        classUnderTest.invoke(null, null)

        verify(bitcoinRepository).getMarketPrice(null, null)
    }
}