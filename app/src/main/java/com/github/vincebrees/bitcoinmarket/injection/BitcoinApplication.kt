package com.github.vincebrees.bitcoinmarket.injection

import android.app.Application
import org.koin.android.ext.android.startKoin
import org.koin.android.logger.AndroidLogger

/**
 * Created by Vincent ETIENNE on 22/02/2019.
 */

class BitcoinApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin(this, listOf(appModule, presentationModule, domainModule), logger = AndroidLogger())
    }
}

