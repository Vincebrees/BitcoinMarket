package com.github.vincebrees.bitcoinmarket.presentation.marketprice

import android.os.Bundle
import com.github.vincebrees.bitcoinmarket.R
import com.github.vincebrees.bitcoinmarket.presentation.BaseActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : BaseActivity() {
    val marketPriceViewModel : MarketPriceViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        marketPriceViewModel.onCreate()
    }
}
