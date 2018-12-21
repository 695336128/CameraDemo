package com.zhang.common.commonutils

import com.orhanobut.logger.LogStrategy

/**
 * Created by zhang .
 * DATA: 2018/8/8 .
 * Description : 将日志文件保存到sd
 */
class DiskLogStrategy: LogStrategy {
    

    override fun log(priority: Int, tag: String?, message: String) {

    }

    fun <T> checkNotNull(obj: T?): T {
        if (obj == null) {
            throw NullPointerException()
        }
        return obj
    }
}