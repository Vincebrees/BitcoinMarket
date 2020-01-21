package com.github.vincebrees.bitcoinmarket.presentation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

abstract class BaseViewModel(
    private val dispatcher: CoroutineDispatcher
) : ViewModel(), CoroutineScope {

    private var handler : CoroutineExceptionHandler = CoroutineExceptionHandler { _, throwable -> baseHandleException(throwable) }
    private var job: Job = Job()
    override val coroutineContext: CoroutineContext
        get() = dispatcher + job + handler


    override fun onCleared() {
        super.onCleared()
        job.cancelChildren() // Cancel job on activity destroy. After destroy all children jobs will be cancelled automatically
    }

    private fun baseHandleException(throwable: Throwable) {
        handleException()
    }

    // Must be overridden by all ViewModels
    abstract fun handleException()
}