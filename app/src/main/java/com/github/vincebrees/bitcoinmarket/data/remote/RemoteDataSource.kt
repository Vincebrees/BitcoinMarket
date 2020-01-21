package com.github.vincebrees.bitcoinmarket.data.remote

import com.github.vincebrees.bitcoinmarket.data.remote.pojo.toEntity
import com.github.vincebrees.bitcoinmarket.domain.entity.BitcoinResponse
import okhttp3.CacheControl


class RemoteDataSource(private var bitcoinService: BitcoinService){

    suspend fun getChartData(urlData: String, timespan : String?, cacheControl : CacheControl?) : BitcoinResponse {
        val response = bitcoinService.getChartData(urlData, timespan, cacheControl.toString())
        return if(response.isSuccessful && response.body() != null){
            response.body()!!.toEntity()
        }else{
            BitcoinResponse.ERROR
        }
    }
}