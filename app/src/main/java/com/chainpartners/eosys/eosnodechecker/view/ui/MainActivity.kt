package com.chainpartners.eosys.eosnodechecker.view.ui

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.chainpartners.eosys.eosnodechecker.R
import com.chainpartners.eosys.eosnodechecker.util.getAppComponent
import com.chainpartners.eosys.eosnodechecker.viewmodel.MainViewModel
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel by lazy { ViewModelProviders.of(this, viewModelFactory).get(MainViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        getAppComponent().inject(this)

        supportFragmentManager.beginTransaction()
                .add(R.id.container, NodeListFragment.newInstance())
                .commit()

        viewModel.apply {
            listItemClickListener.subscribe({
                supportFragmentManager.beginTransaction()
                        .replace(R.id.container, NodeDetailFragment.newInstance(it.name))
                        .addToBackStack(null)
                        .commit()
            }, {
                it.printStackTrace()
            })
        }

        lifecycle.addObserver(viewModel)
    }
}
