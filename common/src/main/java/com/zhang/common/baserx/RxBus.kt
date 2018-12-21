package com.zhang.common.baserx

import com.jakewharton.rxrelay2.PublishRelay
import com.jakewharton.rxrelay2.Relay
import io.reactivex.Observable


/**
 * Created by zhang .
 * DATA: 2018/7/30 .
 * Description : 用RxJava 实现 EventBus
 * 引入J神的rxrelay2,即使出现异常也不会终止订阅关系的 RxRelay
 *
 * EventMsg eventMsg = new EventMsg();
 * eventMsg.setMsg("发送数据");
 * RxBus.getInstance().post(eventMsg);
 */
class RxBus private constructor() {

    private var mBus: Relay<Any> = PublishRelay.create()

    init {
        mBus = mBus.toSerialized()
    }

    companion object {
        val instance by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            RxBus()
        }
    }

    /**
     * 发送事件
     */
    fun post(content: Any) {
        mBus.accept(content)
    }

    fun <T> toObservable(tClass: Class<T>): Observable<T> {
        return mBus.ofType(tClass)
    }

}