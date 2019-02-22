package com.github.vincebrees.bitcoinmarket.presentation.marketprice

import com.github.vincebrees.bitcoinmarket.domain.interactors.GetMarketPriceUseCase
import com.github.vincebrees.bitcoinmarket.presentation.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by Vincent ETIENNE on 22/02/2019.
 */

class MarketPriceViewModel(
    private val getMarketPriceUseCase: GetMarketPriceUseCase
)  : BaseViewModel() {
    fun onCreate() {

    }

    init {
        val disposable = getMarketPriceUseCase.invoke()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                //TODO Handle response

            }, { t: Throwable? ->
                //TODO Handle error
                t!!.printStackTrace()
            })

        compositeDisposable.add(disposable)
    }
}