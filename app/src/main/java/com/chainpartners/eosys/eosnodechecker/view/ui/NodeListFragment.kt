package com.chainpartners.eosys.eosnodechecker.view.ui

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.chainpartners.eosys.eosnodechecker.R
import com.chainpartners.eosys.eosnodechecker.service.model.EosNode
import com.chainpartners.eosys.eosnodechecker.util.getAppComponent
import com.chainpartners.eosys.eosnodechecker.view.adapter.EosNodeAdapter
import com.chainpartners.eosys.eosnodechecker.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.node_list_fragment.*
import javax.inject.Inject

class NodeListFragment : Fragment() {

    companion object {
        fun newInstance() = NodeListFragment()
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel by lazy { ViewModelProviders.of(activity!!, viewModelFactory).get(MainViewModel::class.java) }

    private val adapter = EosNodeAdapter().apply {
        viewClickEvent.subscribe { viewModel.listItemClickEvent(it) }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) = inflater.inflate(R.layout.node_list_fragment, container, false)!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        context!!.getAppComponent().inject(this)

        recyclerView.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            adapter = this@NodeListFragment.adapter
        }

        viewModel.apply {
            nodeList.observe(this@NodeListFragment, Observer<ArrayList<EosNode>> {
                adapter.maxBlockNumber = viewModel.maxBlockNumber
                adapter.setNodeList(it ?: return@Observer)
            })
        }
    }
}