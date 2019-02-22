package com.github.vincebrees.bitcoinmarket.presentation.marketprice

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.github.vincebrees.bitcoinmarket.R
import com.github.vincebrees.bitcoinmarket.presentation.BaseFragment
import kotlinx.android.synthetic.main.fragment_market_price.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*
import java.text.SimpleDateFormat


/**
 * Created by Vincent ETIENNE on 22/02/2019.
 */

class MarketPriceFragment : BaseFragment(){

    private val marketPriceViewModel : MarketPriceViewModel by viewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_market_price, container, false)
    }

    override fun initUI() {
        super.initUI()
        initChart()
    }

    private fun initChart() {
        market_price_chart.apply {
            setTouchEnabled(true)
            isDragEnabled = false
            setScaleEnabled(false)
            legend.isEnabled = false

            description.isEnabled = false
            setPinchZoom(false)
            legend.isEnabled = false

            //Grid Lines
            xAxis.setDrawGridLines(false)
            axisLeft.setDrawGridLines(true)

            //Axis X
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            xAxis.setDrawLabels(true)
            xAxis.textColor = Color.BLACK
            xAxis.axisLineColor = Color.BLACK

            //Axis Left
            axisLeft.setDrawLabels(true)

            //Axis Right
            axisRight.isEnabled = false

            isHighlightPerTapEnabled = true
        }

    }

    override fun initObserver() {
        super.initObserver()
        marketPriceViewModel.liveDataMarketPriceViewState.observe(this, Observer{
            if(it.isLoading){
                showLoading()
            }else{
                hideLoading()
            }

            if(it.isError){
                showError()
            }else{
                hideError()
            }
        })

        marketPriceViewModel.liveDataCurveModel.observe(this, Observer {
            showChart(it.listValues, it.listFormattedDate)
        })
    }


    private fun showChart(values : List<Entry>, dates : List<String>){
        val lineDataSet = LineDataSet(values, "MarketPriceSet")
        lineDataSet.apply {
            color = Color.BLACK
            setCircleColor(Color.BLACK)
            lineWidth = 1f
            circleRadius = 3f
            valueTextSize = 9f
            setDrawCircles(false)
        }

        market_price_chart.apply {
            xAxis.valueFormatter = IAxisValueFormatter { value, axis ->
                dates[value.toInt()]
            }
            data = LineData(lineDataSet)
            notifyDataSetChanged()
        }
    }

    private fun showLoading() {
        //TODO("not implemented")
    }

    private fun hideLoading() {
        //TODO("not implemented")
    }

    private fun showError() {
        //TODO("not implemented")
    }

    private fun hideError() {
        //TODO("not implemented")
    }
}