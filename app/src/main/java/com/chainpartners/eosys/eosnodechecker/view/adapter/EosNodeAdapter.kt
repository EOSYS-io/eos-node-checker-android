package com.chainpartners.eosys.eosnodechecker.view.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.chainpartners.eosys.eosnodechecker.R
import com.chainpartners.eosys.eosnodechecker.service.model.EosNode
import com.chainpartners.eosys.eosnodechecker.util.BLOCK_NUM_INTERVAL
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.item_node.view.*

class EosNodeAdapter : RecyclerView.Adapter<EosNodeAdapter.EosNodeViewHolder>() {

    private val viewClickSubject = PublishSubject.create<EosNode>()
    val viewClickEvent: Observable<EosNode> = viewClickSubject

    private val list = ArrayList<EosNode>()
    var maxBlockNumber = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = EosNodeViewHolder(parent).apply {
        itemView.setOnClickListener { viewClickSubject.onNext(data ?: return@setOnClickListener) }
    }

    override fun onBindViewHolder(holder: EosNodeViewHolder, position: Int) {
        holder.bind(list[position], maxBlockNumber)
    }

    override fun getItemCount() = list.size

    fun setNodeList(list: List<EosNode>) {
        this.list.run {
            clear()
            addAll(list)
            sortBy { it.rank }
        }
        notifyDataSetChanged()
    }


    class EosNodeViewHolder(val parent: ViewGroup) : RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_node, parent, false)) {
        var data: EosNode? = null

        val nodeContainer = itemView.nodeContainer!!
        val nodeTitleText = itemView.nodeTitleText!!
        val nodeNumberText = itemView.nodeNumberText!!

        fun bind(eosNode: EosNode, maxBlockNumber: Int) {
            data = eosNode

            nodeTitleText.text = eosNode.name

            val number = eosNode.info?.head_block_num ?: 0
            nodeNumberText.text = "$number"

            when {
                number == 0 -> R.color.item_background_error
                number < maxBlockNumber - BLOCK_NUM_INTERVAL -> R.color.item_background_warning
                else -> R.color.item_background_default
            }.let { nodeContainer.setBackgroundColor(parent.context.resources.getColor(it)) }
        }
    }
}