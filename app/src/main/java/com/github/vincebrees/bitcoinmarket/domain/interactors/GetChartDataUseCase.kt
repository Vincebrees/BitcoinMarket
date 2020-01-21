package com.github.vincebrees.bitcoinmarket.domain.interactors

import com.github.vincebrees.bitcoinmarket.domain.entity.BitcoinResponse
import com.github.vincebrees.bitcoinmarket.domain.repository.BitcoinRepository

class GetChartDataUseCase(private val bitcoinRepository: BitcoinRepository){
    suspend fun invoke(urlData: String): BitcoinResponse {
        return bitcoinRepository.getChartData(urlData, null, null)
    }
}