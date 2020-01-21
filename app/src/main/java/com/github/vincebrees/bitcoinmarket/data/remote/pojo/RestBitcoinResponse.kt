package com.github.vincebrees.bitcoinmarket.data.remote.pojo

import com.github.vincebrees.bitcoinmarket.domain.entity.BitcoinResponse
import com.github.vincebrees.bitcoinmarket.domain.entity.Coordonate
import com.github.vincebrees.bitcoinmarket.domain.entity.GenericStatus


data class RestBitcoinResponse(
    var status : String?,
    var name : String?,
    var unit : String?,
    var period : String?,
    var description : String?,
    var values : List<RestCoordonate>?
)

fun RestBitcoinResponse.toEntity() : BitcoinResponse {
    return BitcoinResponse(
        GenericStatus.SUCCESS,
        name ?: "",
        unit ?: "",
        period ?: "",
        description ?: "",
        values.toListCoordonate()
    )
}

fun List<RestCoordonate>?.toListCoordonate() : List<Coordonate>{
    return this?.map { Coordonate(it.x, it.y) } ?: arrayListOf()
}