package com.github.vincebrees.bitcoinmarket.data.repository

import com.github.vincebrees.bitcoinmarket.ConstantsTest
import com.github.vincebrees.bitcoinmarket.RxImmediateSchedulerRule
import com.github.vincebrees.bitcoinmarket.data.remote.RemoteDataSource
import com.nhaarman.mockitokotlin2.verify
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
class BitcoinRepositoryImplTest {
    @Rule
    @JvmField
    val rxRule = RxImmediateSchedulerRule()

    @Mock
    lateinit var remoteDataSource: RemoteDataSource

    private lateinit var classUnderTest : BitcoinRepositoryImpl

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        classUnderTest = BitcoinRepositoryImpl(remoteDataSource)
    }

    @Test
    fun getMarketPriceWithTimeSpan(){
        classUnderTest.getMarketPrice(ConstantsTest.TIMESPAN_FILTER, null)

        verify(remoteDataSource).getMarketPrice(ConstantsTest.TIMESPAN_FILTER, null)
    }

    @Test
    fun getMarketPriceWithoutTimeSpan(){
        classUnderTest.getMarketPrice(null, null)

        verify(remoteDataSource).getMarketPrice(null, null)
    }
}