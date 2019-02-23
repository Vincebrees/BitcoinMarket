package com.github.vincebrees.bitcoinmarket.presentation.marketprice

/**
 * Created by Vincent ETIENNE on 22/02/2019.
 */

data class MarketPriceViewState(
    val isLoading: Boolean,
    val isError: Boolean,
    val isRefreshError: Boolean
)