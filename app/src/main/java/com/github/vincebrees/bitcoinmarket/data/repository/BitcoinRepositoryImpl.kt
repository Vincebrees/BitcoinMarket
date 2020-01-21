package com.github.vincebrees.bitcoinmarket.data.repository

import com.github.vincebrees.bitcoinmarket.data.remote.RemoteDataSource
import com.github.vincebrees.bitcoinmarket.domain.entity.BitcoinResponse
import com.github.vincebrees.bitcoinmarket.domain.repository.BitcoinRepository
import okhttp3.CacheControl

class BitcoinRepositoryImpl(private var remoteDataSource: RemoteDataSource) : BitcoinRepository {

    override suspend fun getChartData(urlData: String, timespan: String?, cacheControl: CacheControl?): BitcoinResponse {
        return remoteDataSource.getChartData(urlData, timespan, cacheControl)
    }
}