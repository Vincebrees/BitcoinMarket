package com.github.vincebrees.bitcoinmarket.data.remote

import com.github.vincebrees.bitcoinmarket.data.remote.pojo.RestBitcoinResponse
import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

/**
 * Created by Vincent ETIENNE on 22/02/2019.
 */

interface BitcoinService{
    @GET("/charts/market-price?format=json")
    fun getMarketPrice(
        @Query("timespan") timespan : String?,
        @Header("Cache-Control") cacheControl : String?) : Observable<Response<RestBitcoinResponse>>
}