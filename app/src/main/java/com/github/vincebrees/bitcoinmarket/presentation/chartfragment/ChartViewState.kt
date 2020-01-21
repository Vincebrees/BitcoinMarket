package com.github.vincebrees.bitcoinmarket.presentation.chartfragment

data class ChartViewState(
    val isLoading: Boolean,
    val isError: Boolean,
    val isRefreshError: Boolean
)