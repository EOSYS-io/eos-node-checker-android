package com.chainpartners.eosys.eosnodechecker.viewmodel

import android.arch.lifecycle.*
import com.chainpartners.eosys.eosnodechecker.service.model.EosNode
import com.chainpartners.eosys.eosnodechecker.service.repository.ChainRepository
import com.chainpartners.eosys.eosnodechecker.util.EOSYS_NAME
import com.chainpartners.eosys.eosnodechecker.util.EOSYS_URL
import com.chainpartners.eosys.eosnodechecker.util.TIMEOUT_INTERVAL
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.math.max

class MainViewModel @Inject constructor(
        private val chainRepo: ChainRepository
) : ViewModel(), LifecycleObserver {

    val nodeList = MutableLiveData<ArrayList<EosNode>>()
    val listItemClickListener = PublishSubject.create<EosNode>()!!
    var maxBlockNumber = 0

    private val disposable = CompositeDisposable()
    private var timer: Disposable? = null

    init {
        nodeList.value = ArrayList<EosNode>().apply {
            add(EosNode(EOSYS_NAME, EOSYS_URL))
            add(EosNode("starteosiobp", "http://api-mainnet.starteos.io", 1))
            add(EosNode("eosnewyorkio", "http://api.eosnewyork.io", 2))
            add(EosNode("eoscanadacom", "http://mainnet.eoscanada.com", 3))
            add(EosNode("eoslaomaocom", "https://api.eoslaomao.com", 4))
            add(EosNode("eoshuobipool", "http://peer2.eoshuobipool.com:8181", 5))
            add(EosNode("bitfinexeos1", "http://eos-bp.bitfinex.com:8888", 6))
            add(EosNode("eosfishrocks", "http://api.bp.fish", 7))
            add(EosNode("libertyblock", "http://mainnet.libertyblock.io:8888", 8))
            add(EosNode("eos42freedom", "http://nodes.eos42.io", 9))
            add(EosNode("zbeosbp11111", "https://node1.zbeos.com", 10))
            add(EosNode("eosswedenorg", "http://api.eossweden.se", 11))
            add(EosNode("eosisgravity", "https://api-mainnet.eosgravity.com", 12))
            add(EosNode("eosbeijingbp", "https://api.eosbeijing.one", 13))
            add(EosNode("eoscannonchn", "https://mainnet.eoscannon.io", 14))
            add(EosNode("eosauthority", "https://publicapi-mainnet.eosauthority.com", 15))
            /*add(EosNode("eosbixinboot", "https://mars.fn.eosbixin.com", 16))
            add(EosNode("eosriobrazil", "https://api.eosrio.io", 17))
            add(EosNode("jedaaaaaaaaa", "https://api.jeda.one", 18))
            add(EosNode("argentinaeos", "https://api.eosargentina.io", 19))
            add(EosNode("eosasia11111", "https://api1.eosasia.one", 20))
            add(EosNode("eosdacserver", "https://eu.eosdac.io", 21))*/
            add(EosNode("eoseoul", "http://user-api.eoseoul.io", 54))
            add(EosNode("eosnodeone", "http://api.main-net.eosnodeone.io", 59))
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStart() {
        timer = Observable.interval(0, TIMEOUT_INTERVAL, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { getNodeInfo(nodeList.value ?: return@subscribe) }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onStop() {
        disposable.clear()
        timer?.dispose()
        timer = null
    }

    fun listItemClickEvent(node: EosNode) {
        listItemClickListener.onNext(node)
    }

    private fun getNodeInfo(list: ArrayList<EosNode>) {
        list.forEach { baseNode ->
            disposable.add(
                    chainRepo.getNodeInfo(baseNode)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe({ result ->
                                maxBlockNumber = max(maxBlockNumber, result.info?.head_block_num ?: 0)
                                replaceNode(result)
                            }, {
                                it.printStackTrace()
                                replaceNode(EosNode(baseNode.name, baseNode.url, baseNode.rank))
                            })
            )
        }
    }

    private fun replaceNode(node: EosNode) {
        val list = nodeList.value ?: return
        val origin = list.find { it.name == node.name } ?: return
        nodeList.value = list.apply {
            val index = list.indexOf(origin)
            remove(origin)
            add(index, node)
        }
    }
}