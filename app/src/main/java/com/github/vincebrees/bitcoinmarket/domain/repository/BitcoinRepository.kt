package com.github.vincebrees.bitcoinmarket.domain.repository

import com.github.vincebrees.bitcoinmarket.domain.TypeResponse
import com.github.vincebrees.bitcoinmarket.domain.entity.BitcoinResponse
import io.reactivex.Observable
import okhttp3.CacheControl

/**
 * Created by Vincent ETIENNE on 22/02/2019.
 */

interface BitcoinRepository{
    fun getMarketPrice(timespan: String?, rollingAverage: String?, cacheControl: CacheControl?): Observable<TypeResponse<BitcoinResponse>>
}