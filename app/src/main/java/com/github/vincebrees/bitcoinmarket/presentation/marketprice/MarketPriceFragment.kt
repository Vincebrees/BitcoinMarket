package com.github.vincebrees.bitcoinmarket.presentation.marketprice

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.github.vincebrees.bitcoinmarket.R
import com.github.vincebrees.bitcoinmarket.presentation.BaseFragment
import kotlinx.android.synthetic.main.fragment_market_price.*
import org.koin.androidx.viewmodel.ext.android.viewModel


/**
 * Created by Vincent ETIENNE on 22/02/2019.
 */

class MarketPriceFragment : BaseFragment(){

    private val marketPriceViewModel : MarketPriceViewModel by viewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_market_price, container, false)
    }

    override fun initUI() {
        initChart()
        initButtonListeners()
    }

    private fun initButtonListeners() {
        market_price_btn_filter_30days.setOnClickListener { marketPriceViewModel.onClickedFilter("30days") }
        market_price_btn_filter_60days.setOnClickListener { marketPriceViewModel.onClickedFilter("60days") }
        market_price_btn_filter_180days.setOnClickListener { marketPriceViewModel.onClickedFilter("180days") }
        market_price_btn_filter_1year.setOnClickListener { marketPriceViewModel.onClickedFilter("1year") }
        market_price_btn_filter_2year.setOnClickListener { marketPriceViewModel.onClickedFilter("2year") }
        market_price_btn_filter_alltime.setOnClickListener { marketPriceViewModel.onClickedFilter("all") }

    }

    private fun initChart() {
        market_price_chart.apply {
            setTouchEnabled(true)
            isDragEnabled = false
            setScaleEnabled(false)
            legend.isEnabled = false

            description.isEnabled = false
            setPinchZoom(false)

            //Grid Lines
            xAxis.setDrawGridLines(false)
            axisLeft.setDrawGridLines(true)

            //Axis X
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            xAxis.textColor = Color.BLACK
            xAxis.axisLineColor = Color.BLACK

            //Axis Right
            axisRight.isEnabled = false

            isHighlightPerTapEnabled = true
            visibility = View.INVISIBLE
        }

    }

    override fun initObserver() {
        marketPriceViewModel.liveDataMarketPriceViewState.observe(this, Observer{
            if(it.isLoading) showLoading() else hideLoading()
            if(it.isError) showError() else hideError()
            if(it.isRefreshError) showToast(getString(R.string.generic_error))
        })

        marketPriceViewModel.liveDataCurveModel.observe(this, Observer {
            showChart(it.listValues, it.listFormattedDate)
        })
    }


    private fun showChart(values : List<Entry>, dates : List<String>){
        val lineDataSet = LineDataSet(values, "MarketPriceSet")
        lineDataSet.apply {
            color = ContextCompat.getColor(context!!, R.color.colorAccent)
            setCircleColor(Color.BLACK)
            lineWidth = 1f
            circleRadius = 3f
            valueTextSize = 9f
            setDrawCircles(false)
            setDrawValues(false)
            isHighlightEnabled = true
            setDrawHighlightIndicators(false)
        }

        market_price_chart.apply {
            visibility = View.VISIBLE
            xAxis.valueFormatter = IAxisValueFormatter { value, axis ->
                if (value.toInt() > dates.size - 1) {
                    ""
                }else{
                    dates[value.toInt()]
                }
            }
            data = LineData(lineDataSet)
            notifyDataSetChanged()
            invalidate()
        }
    }

    private fun showLoading() {
        market_price_loader.show()
    }

    private fun hideLoading() {
        market_price_loader.hide()
    }

    private fun showError() {
        market_price_view_error.visibility = View.VISIBLE
    }

    private fun hideError() {
        market_price_view_error.visibility = View.INVISIBLE
    }
}