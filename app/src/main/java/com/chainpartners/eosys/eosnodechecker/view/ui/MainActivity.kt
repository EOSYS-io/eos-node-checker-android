package com.chainpartners.eosys.eosnodechecker.view.ui

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.chainpartners.eosys.eosnodechecker.R
import com.chainpartners.eosys.eosnodechecker.service.model.EosNode
import com.chainpartners.eosys.eosnodechecker.util.getAppComponent
import com.chainpartners.eosys.eosnodechecker.view.adapter.EosNodeAdapter
import com.chainpartners.eosys.eosnodechecker.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.main_activity.*
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val adapter = EosNodeAdapter(null)

    private val viewModel by lazy { ViewModelProviders.of(this, viewModelFactory).get(MainViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        getAppComponent().inject(this)

        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
            adapter = this@MainActivity.adapter
        }

        viewModel.apply {
            nodeList.observe(this@MainActivity, Observer<ArrayList<EosNode>> {
                adapter.setNodeList(it ?: return@Observer)
            })
        }

        lifecycle.addObserver(viewModel)
    }
}
