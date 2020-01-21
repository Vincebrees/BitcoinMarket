package com.github.vincebrees.bitcoinmarket.domain.interactors

import com.github.vincebrees.bitcoinmarket.domain.entity.BitcoinResponse
import com.github.vincebrees.bitcoinmarket.domain.repository.BitcoinRepository
import okhttp3.CacheControl

class UpdateChartDataUseCase(private val bitcoinRepository: BitcoinRepository){
    suspend fun invoke(urlData: String, timeSpan: String?, cacheControl: CacheControl? = null): BitcoinResponse {
        return bitcoinRepository.getChartData(urlData, timeSpan, cacheControl)
    }
}