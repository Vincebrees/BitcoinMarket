package com.github.vincebrees.bitcoinmarket.di

import android.content.Context
import com.github.vincebrees.bitcoinmarket.BuildConfig
import com.github.vincebrees.bitcoinmarket.data.remote.BitcoinService
import com.github.vincebrees.bitcoinmarket.data.remote.RemoteDataSource
import com.github.vincebrees.bitcoinmarket.data.repository.BitcoinRepositoryImpl
import com.github.vincebrees.bitcoinmarket.domain.interactors.GetMarketPriceUseCase
import com.github.vincebrees.bitcoinmarket.domain.interactors.UpdateMarketPriceUseCase
import com.github.vincebrees.bitcoinmarket.domain.repository.BitcoinRepository
import com.github.vincebrees.bitcoinmarket.presentation.marketprice.MarketPriceViewModel
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Created by Vincent ETIENNE on 22/02/2019.
 */

val appModule = module {

    //in here all singleton application class : repositories and data sources
    single<BitcoinRepository> { BitcoinRepositoryImpl(get()) }

    single { RemoteDataSource(get()) }

    single { createOkHttpClient(get()) }

    single { createCache(androidContext()) }

    single { createWebService<BitcoinService>(get(), BuildConfig.ENDPOINT) }

}

val presentationModule = module {
    //all view model declared here
    viewModel { MarketPriceViewModel(get(), get()) }
}

val domainModule = module {
    //all domain interactors declared here, with factory scope
    factory { GetMarketPriceUseCase(get()) }
    factory { UpdateMarketPriceUseCase(get()) }
}


fun createOkHttpClient(cache : Cache): OkHttpClient {
    val httpLoggingInterceptor = HttpLoggingInterceptor()
    httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

    return OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .cache(cache)
        .addInterceptor(httpLoggingInterceptor)
        .build()
}

fun createCache(context: Context): Cache {
    val cacheSize: Long = 10 * 1024 * 1024 //10Mo
    return Cache(context.cacheDir, cacheSize)
}

inline fun <reified T> createWebService(okHttpClient: OkHttpClient, url: String): T {
    val retrofit = Retrofit.Builder()
        .baseUrl(url)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()
    return retrofit.create(T::class.java)
}