package com.github.vincebrees.bitcoinmarket.data.repository

import com.github.vincebrees.bitcoinmarket.data.remote.RemoteDataSource
import com.github.vincebrees.bitcoinmarket.data.remote.pojo.RestBitcoinResponse
import com.github.vincebrees.bitcoinmarket.domain.TypeResponse
import com.github.vincebrees.bitcoinmarket.domain.entity.BitcoinResponse
import com.github.vincebrees.bitcoinmarket.domain.repository.BitcoinRepository
import io.reactivex.Observable

/**
 * Created by Vincent ETIENNE on 22/02/2019.
 */

class BitcoinRepositoryImpl(var remoteDataSource: RemoteDataSource) : BitcoinRepository {

    override fun getMarketPrice(timespan: String, rollingAverage: String): Observable<TypeResponse<BitcoinResponse>> {
        return remoteDataSource.getMarketPrice(timespan, rollingAverage)
    }
}