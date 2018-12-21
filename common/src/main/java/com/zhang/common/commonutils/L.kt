package com.zhang.common.commonutils

import com.orhanobut.logger.*

/**
 * Created by zhang .
 * DATA: 2018/8/8 .
 * Description : 日志工具类
 */
object L {

    private val TAG = "FATE"

    fun init(){
        this.init(false)
    }

    /**
     * 初始化
     */
    fun init(isSaveFile: Boolean){
        val formatStrategy: FormatStrategy = PrettyFormatStrategy.newBuilder()
                .showThreadInfo(false)  // (Optional) Whether to show thread info or not. Default true
                .methodCount(1)         // (Optional) How many method line to show. Default 2
                .methodOffset(7)        // (Optional) Hides internal method calls up to offset. Default 5
                .tag(TAG)                   // (Optional) Global tag for every log. Default PRETTY_LOGGER
                .build()

        val csvFormatStrategy = CsvFormatStrategy.newBuilder()
                .tag(TAG)
                .build()

        Logger.addLogAdapter(AndroidLogAdapter(formatStrategy))
        // 是否存储到文件
        if (isSaveFile){
            Logger.addLogAdapter(DiskLogAdapter(csvFormatStrategy))
        }
        // 只在debug模式下显示
        Logger.addLogAdapter(object : AndroidLogAdapter() {
            override fun isLoggable(priority: Int, tag: String?): Boolean {
                return BuildConfig.DEBUG
            }

        })

    }

    fun v(text: String) {
        Logger.v(text)
    }

    fun d(text: String) {
        Logger.d(text)
    }

    fun i(text: String) {
        Logger.i(text)
    }

    fun w(text: String) {
        Logger.w(text)
    }

    fun e(text: String) {
        Logger.e(text)
    }

    fun wtf(text: String) {
        Logger.wtf(text)
    }

}