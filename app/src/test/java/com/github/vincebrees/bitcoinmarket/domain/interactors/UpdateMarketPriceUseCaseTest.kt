package com.github.vincebrees.bitcoinmarket.domain.interactors

import com.github.vincebrees.bitcoinmarket.ConstantsTest
import com.github.vincebrees.bitcoinmarket.RxImmediateSchedulerRule
import com.github.vincebrees.bitcoinmarket.domain.repository.BitcoinRepository
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
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
    fun testInvoke(){
        classUnderTest.invoke(ConstantsTest.TIMESPAN_FILTER)

        Mockito.verify(bitcoinRepository).getMarketPrice(ConstantsTest.TIMESPAN_FILTER, null)
    }
}