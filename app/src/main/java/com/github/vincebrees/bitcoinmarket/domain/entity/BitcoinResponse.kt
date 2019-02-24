package com.github.vincebrees.bitcoinmarket.domain.entity

/**
 * Created by Vincent ETIENNE on 22/02/2019.
 */

data class BitcoinResponse(
    var status : String,
    var title : String,
    var unit : String,
    var period : String,
    var description : String,
    var values : List<Coordonate>
)