package com.github.vincebrees.bitcoinmarket.domain.interactors

import com.github.vincebrees.bitcoinmarket.RxImmediateSchedulerRule
import com.github.vincebrees.bitcoinmarket.domain.repository.BitcoinRepository
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
class GetMarketPriceUseCaseTest {
    @Rule
    @JvmField
    val rxRule = RxImmediateSchedulerRule()

    @Mock
    lateinit var bitcoinRepository: BitcoinRepository

    private lateinit var classUnderTest : GetMarketPriceUseCase

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        classUnderTest = GetMarketPriceUseCase(bitcoinRepository)
    }

    @Test
    fun testInvoke(){
        classUnderTest.invoke()

        verify(bitcoinRepository).getMarketPrice(null, null)
    }
}