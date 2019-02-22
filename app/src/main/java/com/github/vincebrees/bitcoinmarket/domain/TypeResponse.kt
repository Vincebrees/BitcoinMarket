package com.github.vincebrees.bitcoinmarket.domain

/**
 * Created by Vincent ETIENNE on 22/02/2019.
 */

sealed class TypeResponse<T>

data class DataResponse<T>(val data: T) : TypeResponse<T>()

class ErrorResponse<T>() : TypeResponse<T>() {
    var errorType: ErrorCode? = null

    constructor(errorType: ErrorCode) : this() {
        this.errorType = errorType
    }
}

interface ErrorCode

enum class BitCoinError : ErrorCode {
    UNKNOWN
}


