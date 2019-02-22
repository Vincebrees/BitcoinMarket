package com.github.vincebrees.bitcoinmarket.data.remote

import com.github.vincebrees.bitcoinmarket.data.remote.pojo.RestBitcoinResponse
import com.github.vincebrees.bitcoinmarket.data.remote.pojo.toEntity
import com.github.vincebrees.bitcoinmarket.domain.BitCoinError
import com.github.vincebrees.bitcoinmarket.domain.DataResponse
import com.github.vincebrees.bitcoinmarket.domain.ErrorResponse
import com.github.vincebrees.bitcoinmarket.domain.TypeResponse
import com.github.vincebrees.bitcoinmarket.domain.entity.BitcoinResponse
import io.reactivex.Observable

/**
 * Created by Vincent ETIENNE on 22/02/2019.
 */

class RemoteDataSource(var bitcoinService: BitcoinService){

    fun getMarketPrice(timespan : String?, rollingAverage : String?) : Observable<TypeResponse<BitcoinResponse>> {
        return bitcoinService.getMarketPrice(timespan, rollingAverage).map {
                response -> if(response.isSuccessful && response.body() != null){
            DataResponse(response.body()!!.toEntity())
        }else{
            ErrorResponse<BitcoinResponse>(BitCoinError.UNKNOWN)
        }
        }
    }
}