package com.github.vincebrees.bitcoinmarket.presentation.chartfragment

import com.github.mikephil.charting.data.Entry

data class CurveModel(
    val title : String,
    val description : String,
    val listValues: List<Entry>,
    val listFormattedDate: List<String>
)