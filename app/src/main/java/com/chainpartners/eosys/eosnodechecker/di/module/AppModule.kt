package com.chainpartners.eosys.eosnodechecker.di.module

import android.app.Application
import android.arch.lifecycle.ViewModelProvider
import com.chainpartners.eosys.eosnodechecker.service.repository.ChainRepository
import com.chainpartners.eosys.eosnodechecker.util.EOSYS_URL
import com.chainpartners.eosys.eosnodechecker.viewmodel.factory.ViewModelFactory
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.Executor
import javax.inject.Singleton

@Module
class AppModule(private val app: Application) {

    @Provides
    fun provideApp() = app

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
                .baseUrl(EOSYS_URL)
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