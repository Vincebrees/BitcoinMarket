package com.github.vincebrees.bitcoinmarket.presentation.chartfragment

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
import com.github.vincebrees.bitcoinmarket.R
import com.github.vincebrees.bitcoinmarket.presentation.BaseFragment
import kotlinx.android.synthetic.main.fragment_chart.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf


class ChartFragment : BaseFragment(){
    companion object{
        private const val URL_DATA = "URL_DATA"

        fun newInstance(urlData: String): ChartFragment {
            val fragment = ChartFragment()
            val args = Bundle()
            args.putString(URL_DATA, urlData)
            fragment.arguments = args
            return fragment
        }
    }

    private val urlData: String by lazy { arguments?.getString(URL_DATA) ?: "" }

    private val chartViewModel : ChartViewModel by viewModel{ parametersOf(urlData) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_chart, container, false)
    }

    override fun initUI() {
        initChart()
        initButtonListeners()
        initSwipeRefresh()
    }

    private fun initSwipeRefresh() {
        chart_swipe_refresh.setColorSchemeColors(ContextCompat.getColor(context!!, R.color.colorAccent))
        chart_swipe_refresh.setOnRefreshListener {
            chartViewModel.onRefresh()
        }
    }

    private fun initButtonListeners() {
        chart_btn_filter_30days.setOnClickListener { chartViewModel.onClickedFilter("30days") }
        chart_btn_filter_60days.setOnClickListener { chartViewModel.onClickedFilter("60days") }
        chart_btn_filter_180days.setOnClickListener { chartViewModel.onClickedFilter("180days") }
        chart_btn_filter_1year.setOnClickListener { chartViewModel.onClickedFilter("1year") }
        chart_btn_filter_2year.setOnClickListener { chartViewModel.onClickedFilter("2year") }
        chart_btn_filter_alltime.setOnClickListener { chartViewModel.onClickedFilter("all") }

    }

    private fun initChart() {
        chart.apply {
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
        chartViewModel.liveDataChartViewState.observe(this, Observer{
            if(it.isLoading) showLoading() else hideLoading()
            if(it.isError) showError() else hideError()
            if(it.isRefreshError) showToast(getString(R.string.generic_error))
        })

        chartViewModel.liveDataCurveModel.observe(this, Observer {
            chart_txt_title.text = it.title
            chart_txt_description.text = it.description
            showChart(it.listValues, it.listFormattedDate)
        })
    }


    private fun showChart(values : List<Entry>, dates : List<String>){
        val lineDataSet = LineDataSet(values, "MarketPriceSet")
        lineDataSet.apply {
            color = ContextCompat.getColor(context!!, R.color.chartColor)
            setCircleColor(Color.BLACK)
            lineWidth = 1f
            circleRadius = 3f
            valueTextSize = 9f
            setDrawCircles(false)
            setDrawValues(false)
            isHighlightEnabled = true
            setDrawHighlightIndicators(false)
        }

        chart.apply {
            visibility = View.VISIBLE
//            xAxis.valueFormatter = IAxisValueFormatter { value, axis ->
//                if (value.toInt() > dates.size - 1) {
//                    ""
//                }else{
//                    dates[value.toInt()]
//                }
//            }

            data = LineData(lineDataSet)
            notifyDataSetChanged()
            invalidate()
        }
    }

    private fun showLoading() {
        chart_loader.show()
    }

    private fun hideLoading() {
        chart_swipe_refresh.isRefreshing = false
        chart_loader.hide()
    }

    private fun showError() {
        chart_view_error.visibility = View.VISIBLE
    }

    private fun hideError() {
        chart_view_error.visibility = View.INVISIBLE
    }
}