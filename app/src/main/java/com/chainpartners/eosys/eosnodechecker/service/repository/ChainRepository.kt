package com.chainpartners.eosys.eosnodechecker.service.repository

import com.chainpartners.eosys.eosnodechecker.service.model.EosNode
import io.reactivex.Observable
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChainRepository @Inject constructor(private val retrofit: Retrofit) {
    fun getNodeInfo(eosNode: EosNode): Observable<EosNode> {
        return retrofit.newBuilder()
                .baseUrl(eosNode.url)
                .build()
                .create(ChainService::class.java)
                .getInfo()
                .map { info -> EosNode(eosNode.name, eosNode.url, eosNode.rank, info) }
    }
}