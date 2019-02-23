package com.github.vincebrees.bitcoinmarket.presentation.marketprice

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.github.vincebrees.bitcoinmarket.R
import com.github.vincebrees.bitcoinmarket.presentation.home.HomeActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


/**
 * Created by Vincent ETIENNE on 23/02/2019.
 */
@RunWith(AndroidJUnit4::class)
class MarketPriceFragmentTest{

    @get:Rule
    var activityRule: ActivityTestRule<HomeActivity> = ActivityTestRule(HomeActivity::class.java)

    //Sample test to configure espresso in the project.
    //Although, this test is not really useful, I'm aware of that.
    @Test
    fun testClickButton() {
        onView(withId(R.id.market_price_btn_filter_1year))
            .perform(click())
    }
}