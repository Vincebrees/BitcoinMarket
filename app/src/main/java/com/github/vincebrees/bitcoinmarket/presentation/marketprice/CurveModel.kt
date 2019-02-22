package com.github.vincebrees.bitcoinmarket.presentation.marketprice

import com.github.mikephil.charting.data.Entry
import java.util.*

/**
 * Created by Vincent ETIENNE on 22/02/2019.
 */

data class CurveModel(
    val listValues: List<Entry>,
    val listFormattedDate: List<String>
)