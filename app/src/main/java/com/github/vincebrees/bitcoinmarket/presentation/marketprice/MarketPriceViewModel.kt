package com.github.vincebrees.bitcoinmarket.presentation.marketprice

import androidx.lifecycle.MutableLiveData
import com.github.mikephil.charting.data.Entry
import com.github.vincebrees.bitcoinmarket.domain.DataResponse
import com.github.vincebrees.bitcoinmarket.domain.ErrorResponse
import com.github.vincebrees.bitcoinmarket.domain.entity.BitcoinResponse
import com.github.vincebrees.bitcoinmarket.domain.interactors.GetMarketPriceUseCase
import com.github.vincebrees.bitcoinmarket.domain.interactors.UpdateMarketPriceUseCase
import com.github.vincebrees.bitcoinmarket.presentation.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.text.DateFormat
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

    private var dateFormat = SimpleDateFormat("MMM yy", Locale.getDefault())


    init {
        liveDataMarketPriceViewState.value = MarketPriceViewState(true, false)

        val disposable = getMarketPriceUseCase.invoke()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { response ->
                when(response){
                    is DataResponse -> handleResponseSuccess(response.data)
                    is ErrorResponse -> liveDataMarketPriceViewState.value?.copy(isLoading = false, isError = true)
                }
            }

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

        liveDataMarketPriceViewState.value?.copy(isLoading = false, isError = false)
    }

    fun onClickedFilter(timespan : String) {
        val disposable = updateMarketPriceUseCase.invoke(timespan)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { response ->
                when(response){
                    is DataResponse -> handleResponseSuccess(response.data)
                    is ErrorResponse -> liveDataMarketPriceViewState.value?.copy(isLoading = false, isError = true)
                }
            }

        compositeDisposable.add(disposable)
    }
}