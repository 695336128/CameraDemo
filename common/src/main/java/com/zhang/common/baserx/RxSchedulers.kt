package com.zhang.common.baserx

import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by zhang .
 * DATA: 2018/7/27 .
 * Description : RxJava调度管理
 */
object RxSchedulers {
    fun <T> io_main(): ObservableTransformer<T, T> {
        return ObservableTransformer { observable ->
            observable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
        }
    }

}