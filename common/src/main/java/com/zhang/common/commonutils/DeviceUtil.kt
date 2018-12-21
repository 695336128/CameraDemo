package com.zhang.common.commonutils

import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.os.Build
import android.view.View
import android.view.WindowManager
import java.io.BufferedReader
import java.io.File
import java.io.FileReader


/**
 * Created by zhang .
 * DATA: 2018/7/26 .
 * Description : 设备相关工具类
 */
object DeviceUtil {

    /**
     * 获取设备型号
     */
    fun getModel(): String {
        return Build.MODEL
    }

    /**
     * 获取设备系统版本号
     */
    fun getSDKVersionName(): String{
        return android.os.Build.VERSION.RELEASE
    }

    /**
     * 获取SDK版本号
     */
    fun getSDKVersionCode(): Int {
        return android.os.Build.VERSION.SDK_INT
    }

    /**
     *  获取CPU架构
     */
    fun getABIS(): Array<out String>? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Build.SUPPORTED_ABIS
        } else {
            return null
        }
    }

    /**
     * 获取 eth0 Mac地址
     */
    fun getMacAddress(): String? {
        val fileName = "/sys/class/net/eth0/address"
        val reader = BufferedReader(FileReader(fileName), 256)
        try {
            return reader.readLine()
        }catch (e: Exception){
            return  null
        } finally {
            reader.close()
        }
    }

    /**
     * 获取内存信息
     */
    fun getMemoryStatus(context: Context): String {
        val stringBuilder = StringBuilder()
        val activityManager = context.applicationContext.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        //最大分配内存
        val memory = activityManager.memoryClass
        stringBuilder.append("memory: $memory")
        //最大分配内存获取方法2
        val maxMemory = (Runtime.getRuntime().maxMemory() * 1.0 / (1024 * 1024)).toFloat()
        //当前分配的总内存
        val totalMemory = (Runtime.getRuntime().totalMemory() * 1.0 / (1024 * 1024)).toFloat()
        //剩余内存
        val freeMemory = (Runtime.getRuntime().freeMemory() * 1.0 / (1024 * 1024)).toFloat()
        stringBuilder.append("  maxMemory: $maxMemory")
        stringBuilder.append("  totalMemory: $totalMemory")
        stringBuilder.append("  freeMemory: $freeMemory")
        return stringBuilder.toString()
    }

    /**
     * 设置全屏显示
     */
    fun setFullScreen(act: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            val decorView = act.window.decorView
            decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
        }
    }

    /**
     * 设置屏幕常亮
     */
    fun setKeepScreenOn(act: Activity) {
        act.window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    /**
     * Return whether device is rooted.
     *
     * @return `true`: yes<br></br>`false`: no
     */
    fun isDeviceRooted(): Boolean {
        val su = "su"
        val locations = arrayOf("/system/bin/", "/system/xbin/", "/sbin/", "/system/sd/xbin/", "/system/bin/failsafe/", "/data/local/xbin/", "/data/local/bin/", "/data/local/")
        for (location in locations) {
            if (File(location + su).exists()) {
                return true
            }
        }
        return false
    }



}