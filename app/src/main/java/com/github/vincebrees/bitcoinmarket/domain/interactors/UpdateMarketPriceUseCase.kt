package com.github.vincebrees.bitcoinmarket.domain.interactors

import com.github.vincebrees.bitcoinmarket.domain.TypeResponse
import com.github.vincebrees.bitcoinmarket.domain.entity.BitcoinResponse
import com.github.vincebrees.bitcoinmarket.domain.repository.BitcoinRepository
import io.reactivex.Observable
import okhttp3.CacheControl

/**
 * Created by Vincent ETIENNE on 22/02/2019.
 */

class UpdateMarketPriceUseCase(private val bitcoinRepository: BitcoinRepository){
    fun invoke(timeSpan: String?, cacheControl: CacheControl? = null): Observable<TypeResponse<BitcoinResponse>> {
        return bitcoinRepository.getMarketPrice(timeSpan, cacheControl)
    }
}