package com.chainpartners.eosys.eosnodechecker.di.module

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.chainpartners.eosys.eosnodechecker.viewmodel.MainViewModel
import com.chainpartners.eosys.eosnodechecker.viewmodel.factory.ViewModelFactory
import com.chainpartners.eosys.eosnodechecker.viewmodel.factory.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    internal abstract fun mainViewModel(viewModel: MainViewModel): ViewModel
}