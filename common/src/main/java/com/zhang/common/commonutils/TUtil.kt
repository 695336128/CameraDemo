package com.zhang.common.commonutils

import java.lang.reflect.ParameterizedType

/**
 * Created by zhang .
 * DATA: 2018/7/22 .
 * Description : 类转换初始化
 */
object TUtil {

    /**
     * 根据泛型获取类
     */
    fun <T> getT(o: Any, i: Int): T? {
        try {
            return ((o.javaClass
                    .genericSuperclass as ParameterizedType).actualTypeArguments[i] as Class<T>)
                    .newInstance()
        } catch (e: InstantiationException) {
        } catch (e: IllegalAccessException) {
        } catch (e: ClassCastException) {
        }
        return null
    }

    /**
     * 通过类名获取类
     */
    fun forName(className: String): Class<*>? {
        try {
            return Class.forName(className)
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        }
        return null
    }


}