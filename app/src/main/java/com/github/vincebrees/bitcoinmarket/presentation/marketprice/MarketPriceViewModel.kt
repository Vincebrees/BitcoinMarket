package com.github.vincebrees.bitcoinmarket.presentation.marketprice

import androidx.lifecycle.MutableLiveData
import com.github.mikephil.charting.data.Entry
import com.github.vincebrees.bitcoinmarket.common.Constants
import com.github.vincebrees.bitcoinmarket.domain.BitCoinError
import com.github.vincebrees.bitcoinmarket.domain.DataResponse
import com.github.vincebrees.bitcoinmarket.domain.ErrorCode
import com.github.vincebrees.bitcoinmarket.domain.ErrorResponse
import com.github.vincebrees.bitcoinmarket.domain.entity.BitcoinResponse
import com.github.vincebrees.bitcoinmarket.domain.interactors.GetMarketPriceUseCase
import com.github.vincebrees.bitcoinmarket.domain.interactors.UpdateMarketPriceUseCase
import com.github.vincebrees.bitcoinmarket.presentation.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.CacheControl
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
    private var lastTimeSpan : String = "1year"

    init {
        liveDataMarketPriceViewState.value = MarketPriceViewState(true, false, false)

        val disposable = getMarketPriceUseCase.invoke()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe ({ response ->
                when(response){
                    is DataResponse -> handleResponseSuccess(response.data)
                    is ErrorResponse -> handleGetDataError(response.errorType)
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

        liveDataCurveModel.value = CurveModel(bitcoinResponse.title, bitcoinResponse.description, listEntry, listDate)

        liveDataMarketPriceViewState.value = liveDataMarketPriceViewState.value?.copy(isLoading = false, isError = false, isRefreshError = false)
    }

    fun onClickedFilter(timespan : String) {
        liveDataMarketPriceViewState.value = liveDataMarketPriceViewState.value?.copy(isLoading = true, isRefreshError = false)

        lastTimeSpan = timespan

        val disposable = updateMarketPriceUseCase.invoke(timespan)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe ({ response ->
                when(response){
                    is DataResponse -> handleResponseSuccess(response.data)
                    is ErrorResponse -> handleRefreshError(response.errorType)
                }
            }, {
                handleRefreshError()
            })

        compositeDisposable.add(disposable)
    }

    fun onRefresh(){
        val disposable = updateMarketPriceUseCase.invoke(lastTimeSpan, CacheControl.FORCE_NETWORK)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe ({ response ->
                when(response){
                    is DataResponse -> handleResponseSuccess(response.data)
                    is ErrorResponse -> handleRefreshError(response.errorType)
                }
            }, {
                handleRefreshError()
            })

        compositeDisposable.add(disposable)
    }


    private fun handleGetDataError(errorType: ErrorCode? = BitCoinError.UNKNOWN) {
        //Multiples type of error can be handled here
        when(errorType as BitCoinError){
            BitCoinError.UNKNOWN -> liveDataMarketPriceViewState.value = liveDataMarketPriceViewState.value?.copy(isLoading = false, isError = true)
        }
    }

    private fun handleRefreshError(errorType: ErrorCode? = BitCoinError.UNKNOWN) {
        //Multiples type of error can be handled here
        when(errorType as BitCoinError){
            BitCoinError.UNKNOWN -> liveDataMarketPriceViewState.value = liveDataMarketPriceViewState.value?.copy(isLoading = false, isRefreshError = true)
        }
    }
}