package com.github.vincebrees.bitcoinmarket.presentation.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.github.vincebrees.bitcoinmarket.R
import com.github.vincebrees.bitcoinmarket.presentation.BaseActivity
import com.github.vincebrees.bitcoinmarket.presentation.chartfragment.ChartFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        goTo(ChartFragment.newInstance("market-price"))

        initBottomBar()
    }

    private fun initBottomBar() {
        val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { menuItem ->
            when(menuItem.itemId){
                R.id.action_home -> {
                    goTo(ChartFragment.newInstance("market-price"))
                    return@OnNavigationItemSelectedListener true
                }
                R.id.action_transactions -> {
                    goTo(ChartFragment.newInstance("transactions-per-second"))
                    return@OnNavigationItemSelectedListener true
                }
                R.id.action_capitalisation -> {
                    goTo(ChartFragment.newInstance("market-cap"))
                    return@OnNavigationItemSelectedListener true
                }
            }
            return@OnNavigationItemSelectedListener false
        }
        home_bottom_bar.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }

    private fun goTo(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(
                R.id.fragment_container,
                fragment
            )
            .commit()
    }
}
