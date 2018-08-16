package com.chainpartners.eosys.eosnodechecker.view.ui

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.chainpartners.eosys.eosnodechecker.R
import com.chainpartners.eosys.eosnodechecker.service.model.EosChainInfo
import com.chainpartners.eosys.eosnodechecker.service.model.EosNode
import com.chainpartners.eosys.eosnodechecker.util.BLOCK_NUM_INTERVAL
import com.chainpartners.eosys.eosnodechecker.util.getAppComponent
import com.chainpartners.eosys.eosnodechecker.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.node_detail_fragment.*
import javax.inject.Inject

class NodeDetailFragment : Fragment() {

    companion object {
        const val TITLE_KEY = "title"

        fun newInstance(title: String): NodeDetailFragment {
            return NodeDetailFragment().apply {
                arguments = Bundle().apply {
                    putString(TITLE_KEY, title)
                }
            }
        }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel by lazy { ViewModelProviders.of(activity!!, viewModelFactory).get(MainViewModel::class.java) }

    private var title: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) = inflater.inflate(R.layout.node_detail_fragment, container, false)!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        context!!.getAppComponent().inject(this)
        setHasOptionsMenu(true)

        title = arguments?.getString(TITLE_KEY)

        viewModel.apply {
            nodeList.observe(this@NodeDetailFragment, Observer<ArrayList<EosNode>> { list ->
                val data = list?.find { it.name == title } ?: return@Observer

                nodeTitleText.text = data.name

                val info = data.info ?: EosChainInfo("none", 0, 0, "none", "none", "none")
                nodeNumberText.text = "Number : ${info.head_block_num}"
                nodeIdText.text = "ID : ${info.head_block_id}"
                nodeTimeText.text = "Time : ${info.head_block_time}"
                nodeProducerText.text = "Producer : ${info.head_block_producer}"

                when {
                    info.head_block_num == 0 -> R.color.item_background_error
                    info.head_block_num < viewModel.maxBlockNumber - BLOCK_NUM_INTERVAL -> R.color.item_background_warning
                    else -> R.color.item_background_default
                }.let { nodeContainer.setBackgroundColor(resources.getColor(it)) }
            })
        }
    }

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity?)?.supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onPause() {
        super.onPause()
        (activity as AppCompatActivity?)?.supportActionBar?.setDisplayHomeAsUpEnabled(false)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home) {
            activity?.onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}