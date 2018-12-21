package com.zhang.common.baseapp

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.res.Resources
import android.support.multidex.MultiDex

/**
 * Created by zhang .
 * DATA: 2018/7/21 .
 * Description : APPLICATION
 */
open class BaseApplication : Application(){

    val TAG = "FATE"

    companion object {
        @SuppressLint("StaticFieldLeak")
        var context: Context? = null

        fun getAppResources():Resources{
            return context?.resources!!
        }
    }

    override fun onCreate() {
        super.onCreate()
        context = this
    }

    /**
     * 分包
     */
    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }





}
