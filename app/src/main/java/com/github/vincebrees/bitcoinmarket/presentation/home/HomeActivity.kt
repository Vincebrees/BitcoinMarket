package com.github.vincebrees.bitcoinmarket.presentation.home

import android.os.Bundle
import com.github.vincebrees.bitcoinmarket.R
import com.github.vincebrees.bitcoinmarket.presentation.BaseActivity
import com.github.vincebrees.bitcoinmarket.presentation.marketprice.MarketPriceFragment

class HomeActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_activity)

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container,
                MarketPriceFragment()
            )
            .commit()
    }
}
