package com.chainpartners.eosys.eosnodechecker.viewmodel

import android.arch.lifecycle.*
import com.chainpartners.eosys.eosnodechecker.service.model.EosNode
import com.chainpartners.eosys.eosnodechecker.service.repository.ChainRepository
import com.chainpartners.eosys.eosnodechecker.util.EOSYS_NAME
import com.chainpartners.eosys.eosnodechecker.util.EOSYS_URL
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class MainViewModel @Inject constructor(
        private val chainRepo: ChainRepository
) : ViewModel(), LifecycleObserver {

    val nodeList = MutableLiveData<ArrayList<EosNode>>()

    private val disposable = CompositeDisposable()
    private var timer: Disposable? = null

    init {
        nodeList.value = ArrayList<EosNode>().apply {
            add(EosNode(EOSYS_NAME, EOSYS_URL))
            add(EosNode("starteosiobp", "http://api-mainnet.starteos.io", 1))
            add(EosNode("eoscanadacom", "http://mainnet.eoscanada.com", 2))
            add(EosNode("eosnewyorkio", "http://api.eosnewyork.io", 3))
            add(EosNode("eoshuobipool", "http://peer1.eoshuobipool.com:8181", 4))
            add(EosNode("zbeosbp11111", "https://node1.zbeos.com", 5))
            add(EosNode("bitfinexeos1", "http://eos-bp.bitfinex.com:8888", 6))
            add(EosNode("libertyblock", "http://mainnet.libertyblock.io:8888", 7))
            add(EosNode("eos42freedom", "http://nodes.eos42.io", 8))
            add(EosNode("eosfishrocks", "http://api.bp.fish", 9))
            add(EosNode("eosswedenorg", "http://api.eossweden.se", 10))
            add(EosNode("eoseoul", "http://user-api.eoseoul.io", 54))
            add(EosNode("eosnodeone", "http://api.main-net.eosnodeone.io", 59))
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStart() {
        timer = Observable.interval(0, 3, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { getNodeList(nodeList.value ?: return@subscribe) }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onStop() {
        disposable.clear()
        timer?.dispose()
        timer = null
    }

    private fun getNodeList(list: ArrayList<EosNode>) {
        list.forEach { baseNode ->
            disposable.add(
                    chainRepo.getNodeInfo(baseNode)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe({ result ->
                                nodeList.value?.let { list ->
                                    list.find { it.name == result.name }?.let { origin ->
                                        nodeList.value = list.apply {
                                            remove(origin)
                                            add(result)
                                        }
                                    }
                                }
                            }, {
                                it.printStackTrace()
                            })
            )
        }
    }
}