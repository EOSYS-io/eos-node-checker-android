package com.chainpartners.eosys.eosnodechecker.util

import android.content.Context
import com.chainpartners.eosys.eosnodechecker.app.App
import com.chainpartners.eosys.eosnodechecker.di.component.AppComponent

fun Context.getAppComponent(): AppComponent {
    return (applicationContext as App).appComponent
}