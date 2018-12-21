package com.zhang.common.baserx

import com.zhang.common.basebean.BaseRespose
import io.reactivex.Observable
import io.reactivex.ObservableTransformer


/**
 * Created by zhang .
 * DATA: 2018/8/3 .
 * Description : 对服务器返回数据成功和失败处理
 */
object RxHelper {
    fun <T> handleResult(): ObservableTransformer<BaseRespose<T>, T> {
        return ObservableTransformer { upstream ->
            upstream.flatMap { result ->
                if (result.success()) {
                    return@flatMap createData(result.data)
                } else {
                    return@flatMap Observable.error<T>(ServerException(result.msg ?: "UNKNOWN SERVER ERROR"))
                }
            }
        }
    }

    /**
     * 创建成功的数据
     *
     * @param data
     * @param <T>
     * @return
     */
    private fun <T> createData(data: T): Observable<T> {
        return Observable.create { emitter ->
            try {
                emitter.onNext(data)
                emitter.onComplete()
            } catch (e: Exception) {
                e.printStackTrace()
                emitter.onError(e)
            }
        }

    }

}