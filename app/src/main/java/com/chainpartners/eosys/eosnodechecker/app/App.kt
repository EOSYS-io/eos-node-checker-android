package com.chainpartners.eosys.eosnodechecker.app

import android.app.Application
import com.chainpartners.eosys.eosnodechecker.di.component.AppComponent
import com.chainpartners.eosys.eosnodechecker.di.component.DaggerAppComponent
import com.chainpartners.eosys.eosnodechecker.di.module.AppModule

class App : Application() {

    val appComponent: AppComponent by lazy { DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .build() }

    override fun onCreate() {
        super.onCreate()
        appComponent.inject(this)
    }
}