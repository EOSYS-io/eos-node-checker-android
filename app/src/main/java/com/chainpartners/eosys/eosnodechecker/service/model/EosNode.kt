package com.chainpartners.eosys.eosnodechecker.service.model

data class EosNode(
        val name: String,
        val url: String,
        val rank: Int = 0,
        val info: EosChainInfo? = null
)