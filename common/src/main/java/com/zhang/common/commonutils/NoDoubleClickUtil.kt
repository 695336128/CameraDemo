package com.zhang.common.commonutils

/**
 * Created by zhang .
 * DATA: 2018/1/23 .
 * Description : 防止按键重复点击
 */

object NoDoubleClickUtil {
    private var lastClickTime: Long = 0
    private const val SPACE_TIME = 500

    val isDoubleClick: Boolean
        @Synchronized get() {
            val currentTime = System.currentTimeMillis()
            val isClick2: Boolean
            isClick2 = currentTime - lastClickTime <= SPACE_TIME
            lastClickTime = currentTime
            return isClick2
        }
}
