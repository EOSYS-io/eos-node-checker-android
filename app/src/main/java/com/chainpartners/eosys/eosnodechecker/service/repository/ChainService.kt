package com.chainpartners.eosys.eosnodechecker.service.repository

import com.chainpartners.eosys.eosnodechecker.service.model.EosChainInfo
import io.reactivex.Observable
import retrofit2.http.POST

interface ChainService {
    @POST("/v1/chain/get_info")
    fun getInfo(): Observable<EosChainInfo>
}