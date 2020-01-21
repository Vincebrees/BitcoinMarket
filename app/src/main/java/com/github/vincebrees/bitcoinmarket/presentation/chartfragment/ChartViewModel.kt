package com.github.vincebrees.bitcoinmarket.presentation.chartfragment

import androidx.lifecycle.MutableLiveData
import com.github.mikephil.charting.data.Entry
import com.github.vincebrees.bitcoinmarket.common.Constants
import com.github.vincebrees.bitcoinmarket.domain.entity.BitcoinResponse
import com.github.vincebrees.bitcoinmarket.domain.entity.GenericStatus
import com.github.vincebrees.bitcoinmarket.domain.interactors.GetChartDataUseCase
import com.github.vincebrees.bitcoinmarket.domain.interactors.UpdateChartDataUseCase
import com.github.vincebrees.bitcoinmarket.presentation.BaseViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import okhttp3.CacheControl
import java.text.SimpleDateFormat
import java.util.*


class ChartViewModel(
    private val urlData: String,
    getChartDataUseCase: GetChartDataUseCase,
    private val updateChartDataUseCase: UpdateChartDataUseCase,
    dispatcher: CoroutineDispatcher
)  : BaseViewModel(dispatcher) {

    val liveDataCurveModel: MutableLiveData<CurveModel> = MutableLiveData()
    val liveDataChartViewState: MutableLiveData<ChartViewState> = MutableLiveData()

    private var dateFormat = SimpleDateFormat(Constants.CHART_DATE_PATTERN, Locale.getDefault())
    private var lastTimeSpan : String = "1year"

    init {
        liveDataChartViewState.value = ChartViewState(isLoading = true, isError = false, isRefreshError =  false)

        launch {
            val bitcoinResponse = getChartDataUseCase.invoke(urlData)
            when(bitcoinResponse.status){
                GenericStatus.SUCCESS -> handleResponseSuccess(bitcoinResponse)
                GenericStatus.ERROR -> handleException()
            }
        }
    }

    private fun handleResponseSuccess(bitcoinResponse: BitcoinResponse) {
        val listEntry = arrayListOf<Entry>()
        val listDate = arrayListOf<String>()

        bitcoinResponse.values.forEach {
            listEntry.add(Entry((listEntry.size + 1).toFloat(), it.y.toFloat()))
            listDate.add(dateFormat.format(Date(it.x * 1000L)))
        }

        liveDataCurveModel.value = CurveModel(bitcoinResponse.title, bitcoinResponse.description, listEntry, listDate)

        liveDataChartViewState.value = liveDataChartViewState.value?.copy(isLoading = false, isError = false, isRefreshError = false)
    }

    fun onClickedFilter(timespan : String) {
        liveDataChartViewState.value = liveDataChartViewState.value?.copy(isLoading = true, isRefreshError = false)

        lastTimeSpan = timespan

        val handler = CoroutineExceptionHandler { _, _ -> handleRefreshError() }

        launch(handler) {
         val bitcoinResponse = updateChartDataUseCase.invoke(urlData, timespan)
            when(bitcoinResponse.status){
                GenericStatus.SUCCESS -> handleResponseSuccess(bitcoinResponse)
                GenericStatus.ERROR -> handleRefreshError()
            }
        }
    }

    fun onRefresh(){
        val handler = CoroutineExceptionHandler { _, _ -> handleRefreshError() }
        launch(handler) {
            val bitcoinResponse = updateChartDataUseCase.invoke(urlData, lastTimeSpan, CacheControl.FORCE_NETWORK)
            when(bitcoinResponse.status){
                GenericStatus.SUCCESS -> handleResponseSuccess(bitcoinResponse)
                GenericStatus.ERROR -> handleRefreshError()
            }
        }
    }


    override fun handleException() {
        liveDataChartViewState.value = liveDataChartViewState.value?.copy(isLoading = false, isError = true)
    }

    private fun handleRefreshError() {
        liveDataChartViewState.value = liveDataChartViewState.value?.copy(isLoading = false, isRefreshError = true)
    }
}