package com.zhang.common.baserx

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer


/**
 * Created by zhang .
 * DATA: 2018/7/27 .
 * Description : 用于管理单个presenter的RxBus的事件和Rxjava相关代码的生命周期处理
 *
 * RxManager.post()
 * mRxManage.onEvent(TestBean::class.java, object : Consumer<TestBean>{
 *   override fun accept(t: TestBean?) {
 *       println(t.toString())
 *      }
 *  })
 *
 */
class RxManager {
    // 管理Observables 和 Subscribers订阅
    private val mCompositeDisposable = CompositeDisposable()

    private val rxBus: RxBus = RxBus.instance

    /**
     * 订阅者管理
     */
    fun add(disposable: Disposable) {
        mCompositeDisposable.add(disposable)
    }

    /**
     * 单个presenter生命周期结束，取消订阅和所有rxbus观察
     */
    fun clear() {
        mCompositeDisposable.clear()
    }

    fun <T> onEvent(cls: Class<T>, mConsumer: Consumer<T>) {
        rxBus.toObservable(cls)
                .subscribe(mConsumer)
    }

    fun post(content: Any) {
        rxBus.post(content)
    }
}