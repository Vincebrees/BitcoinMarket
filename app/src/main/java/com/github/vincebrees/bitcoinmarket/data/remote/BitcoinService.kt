package com.github.vincebrees.bitcoinmarket.data.remote

import com.github.vincebrees.bitcoinmarket.data.remote.pojo.RestBitcoinResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query


interface BitcoinService{
    @GET("/charts/{urlData}?format=json")
    suspend fun getChartData(
        @Path("urlData") urlData: String,
        @Query("timespan") timespan : String?,
        @Header("Cache-Control") cacheControl : String?) : Response<RestBitcoinResponse>
}