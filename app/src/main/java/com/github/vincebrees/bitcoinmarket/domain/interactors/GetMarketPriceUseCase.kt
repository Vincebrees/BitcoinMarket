package com.github.vincebrees.bitcoinmarket.domain.interactors

import com.github.vincebrees.bitcoinmarket.domain.TypeResponse
import com.github.vincebrees.bitcoinmarket.domain.entity.BitcoinResponse
import com.github.vincebrees.bitcoinmarket.domain.repository.BitcoinRepository
import io.reactivex.Observable

/**
 * Created by Vincent ETIENNE on 22/02/2019.
 */

class GetMarketPriceUseCase(private val bitcoinRepository: BitcoinRepository){
    fun invoke(): Observable<TypeResponse<BitcoinResponse>> {
        return bitcoinRepository.getMarketPrice(null, null, null)
    }
}