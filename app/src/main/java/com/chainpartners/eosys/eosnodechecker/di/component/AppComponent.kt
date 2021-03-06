package com.chainpartners.eosys.eosnodechecker.di.component

import com.chainpartners.eosys.eosnodechecker.app.App
import com.chainpartners.eosys.eosnodechecker.di.module.AppModule
import com.chainpartners.eosys.eosnodechecker.di.module.ViewModelModule
import com.chainpartners.eosys.eosnodechecker.view.ui.MainActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, ViewModelModule::class])
interface AppComponent {
    fun inject(app: App)
    fun inject(activity: MainActivity)
}