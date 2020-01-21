package com.github.vincebrees.bitcoinmarket.domain.entity

data class BitcoinResponse(
    var status : GenericStatus,
    var title : String,
    var unit : String,
    var period : String,
    var description : String,
    var values : List<Coordonate>
){
    companion object {
        val ERROR = BitcoinResponse(GenericStatus.ERROR, "", "", "", "", arrayListOf())
    }
}