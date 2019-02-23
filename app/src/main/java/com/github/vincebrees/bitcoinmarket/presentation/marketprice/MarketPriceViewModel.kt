package com.github.vincebrees.bitcoinmarket.presentation.marketprice

import androidx.lifecycle.MutableLiveData
import com.github.mikephil.charting.data.Entry
import com.github.vincebrees.bitcoinmarket.common.Constants
import com.github.vincebrees.bitcoinmarket.domain.DataResponse
import com.github.vincebrees.bitcoinmarket.domain.ErrorResponse
import com.github.vincebrees.bitcoinmarket.domain.entity.BitcoinResponse
import com.github.vincebrees.bitcoinmarket.domain.interactors.GetMarketPriceUseCase
import com.github.vincebrees.bitcoinmarket.domain.interactors.UpdateMarketPriceUseCase
import com.github.vincebrees.bitcoinmarket.presentation.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Vincent ETIENNE on 22/02/2019.
 */

class MarketPriceViewModel(
    private val getMarketPriceUseCase: GetMarketPriceUseCase,
    private val updateMarketPriceUseCase: UpdateMarketPriceUseCase
)  : BaseViewModel() {

    val liveDataCurveModel: MutableLiveData<CurveModel> = MutableLiveData()
    val liveDataMarketPriceViewState: MutableLiveData<MarketPriceViewState> = MutableLiveData()

    private var dateFormat = SimpleDateFormat(Constants.CHART_DATE_PATTERN, Locale.getDefault())


    init {
        liveDataMarketPriceViewState.value = MarketPriceViewState(true, false, false)

        val disposable = getMarketPriceUseCase.invoke()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe ({ response ->
                when(response){
                    is DataResponse -> handleResponseSuccess(response.data)
                    is ErrorResponse -> handleGetDataError()
                }
            }, {
                handleGetDataError()
            })


        compositeDisposable.add(disposable)
    }

    private fun handleResponseSuccess(bitcoinResponse: BitcoinResponse) {
        val listEntry = arrayListOf<Entry>()
        val listDate = arrayListOf<String>()

        bitcoinResponse.values.forEach {
            listEntry.add(Entry((listEntry.size + 1).toFloat(), it.y.toFloat()))
            listDate.add(dateFormat.format(Date(it.x * 1000L)))
        }

        liveDataCurveModel.value = CurveModel(listEntry, listDate)

        liveDataMarketPriceViewState.value = liveDataMarketPriceViewState.value?.copy(isLoading = false, isError = false, isRefreshError = false)
    }

    fun onClickedFilter(timespan : String) {
        liveDataMarketPriceViewState.value = liveDataMarketPriceViewState.value?.copy(isLoading = true, isRefreshError = false)

        val disposable = updateMarketPriceUseCase.invoke(timespan)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe ({ response ->
                when(response){
                    is DataResponse -> handleResponseSuccess(response.data)
                    is ErrorResponse -> handleRefreshError()
                }
            }, {
                handleRefreshError()
            })

        compositeDisposable.add(disposable)
    }


    private fun handleGetDataError() {
        liveDataMarketPriceViewState.value = liveDataMarketPriceViewState.value?.copy(isLoading = false, isError = true)
    }

    private fun handleRefreshError() {
        liveDataMarketPriceViewState.value = liveDataMarketPriceViewState.value?.copy(isLoading = false, isRefreshError = true)
    }
}