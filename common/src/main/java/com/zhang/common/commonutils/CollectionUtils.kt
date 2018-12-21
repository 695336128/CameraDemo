package com.zhang.common.commonutils

/**
 * Created by zhang .
 * DATA: 2018/7/26 .
 * Description :
 */
object CollectionUtils {
    /**
     * 判断集合是否为null或者0个元素
     *
     * @param c
     * @return
     */
    fun isNullOrEmpty(c: Collection<*>?): Boolean {
        return null == c || c.isEmpty()
    }
}