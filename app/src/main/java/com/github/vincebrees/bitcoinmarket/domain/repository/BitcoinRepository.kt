package com.github.vincebrees.bitcoinmarket.domain.repository

import com.github.vincebrees.bitcoinmarket.domain.entity.BitcoinResponse
import okhttp3.CacheControl

interface BitcoinRepository{
    suspend fun getChartData(urlData: String, timespan: String?, cacheControl: CacheControl?): BitcoinResponse
}