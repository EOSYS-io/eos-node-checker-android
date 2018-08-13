package com.chainpartners.eosys.eosnodechecker.view.adapter

import android.annotation.SuppressLint
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.chainpartners.eosys.eosnodechecker.R
import com.chainpartners.eosys.eosnodechecker.service.model.EosChainInfo
import com.chainpartners.eosys.eosnodechecker.service.model.EosNode
import com.chainpartners.eosys.eosnodechecker.util.BLOCK_NUM_INTERVAL
import kotlinx.android.synthetic.main.item_node.view.*

class EosNodeAdapter(
        private val clickListener: ((eosNode: EosNode) -> Unit)?
) : RecyclerView.Adapter<EosNodeAdapter.EosNodeViewHolder>() {

    private val list = ArrayList<EosNode>()
    var maxBlockNumber = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = EosNodeViewHolder(parent, clickListener)

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


    class EosNodeViewHolder(
            val parent: ViewGroup,
            private val clickListener: ((eosNode: EosNode) -> Unit)?
    ) : RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_node, parent, false)) {
        var data: EosNode? = null

        val nodeContainer = itemView.nodeContainer!!
        val nodeTitleText = itemView.nodeTitleText!!
        val nodeNumberText = itemView.nodeNumberText!!
        val nodeIdText = itemView.nodeIdText!!
        val nodeTimeText = itemView.nodeTimeText!!
        val nodeProducerText = itemView.nodeProducerText!!

        init {
            itemView.setOnClickListener { clickListener?.invoke(data ?: return@setOnClickListener) }
        }

        @SuppressLint("SetTextI18n")
        fun bind(eosNode: EosNode, maxBlockNumber: Int) {
            data = eosNode

            nodeTitleText.text = eosNode.name

            val info = eosNode.info ?: EosChainInfo("none", 0, 0, "none", "none", "none")
            nodeNumberText.text = "Number : ${info.head_block_num}"
            nodeIdText.text = "ID : ${info.head_block_id}"
            nodeTimeText.text = "Time : ${info.head_block_time}"
            nodeProducerText.text = "Producer : ${info.head_block_producer}"

            when {
                info.head_block_num == 0 -> R.color.item_background_error
                info.head_block_num < maxBlockNumber - BLOCK_NUM_INTERVAL -> R.color.item_background_warning
                else -> R.color.item_background_default
            }.let { nodeContainer.setBackgroundColor(parent.context.resources.getColor(it)) }
        }
    }
}