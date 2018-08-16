package com.chainpartners.eosys.eosnodechecker.di.module

import android.app.Application
import com.chainpartners.eosys.eosnodechecker.service.repository.ChainRepository
import com.chainpartners.eosys.eosnodechecker.util.EOSYS_URL
import com.chainpartners.eosys.eosnodechecker.util.TIMEOUT_INTERVAL
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class AppModule(private val app: Application) {

    @Provides
    fun provideApp() = app

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
                .connectTimeout(TIMEOUT_INTERVAL, TimeUnit.SECONDS)
                .readTimeout(TIMEOUT_INTERVAL, TimeUnit.SECONDS)
                .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
                .baseUrl(EOSYS_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
    }

    @Provides
    @Singleton
    fun provideChainRepository(retrofit: Retrofit): ChainRepository {
        return ChainRepository(retrofit)
    }
}