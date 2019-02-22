package com.github.vincebrees.bitcoinmarket.presentation

import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable

/**
 * Created by Vincent ETIENNE on 22/02/2019.
 */

abstract class BaseViewModel : ViewModel(){

    val compositeDisposable = CompositeDisposable()


    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

}