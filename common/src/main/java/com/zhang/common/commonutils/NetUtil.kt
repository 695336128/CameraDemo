package com.zhang.common.commonutils

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.wifi.WifiManager
import android.telephony.TelephonyManager
import java.net.NetworkInterface
import java.net.SocketException

/**
 * Created by zhang .
 * DATA: 2018/8/8 .
 * Description : 网络相关工具类
 */
object NetUtil {

    /**
     * 打开网络设置界面
     */
    fun openWirelessSettings(context: Context){
        val intent = Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
    }

    /**
     * 获取活动网络信息
     * <p>需添加权限
     * {@code <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />}</p>
     *
     * @return NetworkInfo
     */
    fun getActiveNetworkInfo(context: Context): NetworkInfo?{
        val manager: ConnectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return manager.activeNetworkInfo
    }

    /**
     * 获取活动网络信息
     * <p>需添加权限
     * {@code <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />}</p>
     *
     * @return {@code true}: 是<br>{@code false}: 否
     */
    fun isConnected(context: Context): Boolean{
        val info: NetworkInfo? = getActiveNetworkInfo(context)
        return info != null && info.isConnected
    }

    /**
     * 判断移动数据是否打开
     *
     * @return {@code true}: 是<br>{@code false}: 否
     */
    fun getMobileDataEnabled(context: Context): Boolean{
        try {
            val tm = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            @SuppressLint("PrivateApi")
            val getMobileDataEnabledMethod = tm.javaClass.getDeclaredMethod("getDataEnabled")
            if (null != getMobileDataEnabledMethod) {
                return getMobileDataEnabledMethod.invoke(tm) as Boolean
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return false
    }

    /**
     * 判断网络是否是移动数据
     *
     * 需添加权限
     * `<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />`
     *
     * @return `true`: 是<br></br>`false`: 否
     */
    @SuppressLint("MissingPermission")
    fun isMobileData(context: Context): Boolean {
        val info = getActiveNetworkInfo(context)
        return (null != info
                && info.isAvailable
                && info.type == ConnectivityManager.TYPE_MOBILE)
    }

    /**
     * 判断 wifi 是否打开
     *
     * 需添加权限
     * `<uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />`
     *
     * @return `true`: 是<br></br>`false`: 否
     */
    fun getWifiEnabled(context: Context): Boolean {
        @SuppressLint("WifiManagerLeak")
        val manager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        return manager != null && manager.isWifiEnabled
    }

    /**
     * 判断 wifi 是否连接状态
     *
     * 需添加权限
     * `<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />`
     *
     * @return `true`: 连接<br></br>`false`: 未连接
     */
    @SuppressLint("MissingPermission")
    fun isWifiConnected(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return (cm.activeNetworkInfo != null
                && cm.activeNetworkInfo.type == ConnectivityManager.TYPE_WIFI)
    }

    /**
     * 获取 IP 地址
     *
     * 需添加权限 `<uses-permission android:name="android.permission.INTERNET" />`
     *
     * @param useIPv4 是否用 IPv4
     * @return IP 地址
     */
    fun getIPAddress(useIPv4: Boolean): String? {
        try {
            val nis = NetworkInterface.getNetworkInterfaces()
            while (nis.hasMoreElements()) {
                val ni = nis.nextElement()
                // 防止小米手机返回 10.0.2.15
                if (!ni.isUp) continue
                val addresses = ni.inetAddresses
                while (addresses.hasMoreElements()) {
                    val inetAddress = addresses.nextElement()
                    if (!inetAddress.isLoopbackAddress) {
                        val hostAddress = inetAddress.hostAddress
                        val isIPv4 = hostAddress.indexOf(':') < 0
                        if (useIPv4) {
                            if (isIPv4) return hostAddress
                        } else {
                            if (!isIPv4) {
                                val index = hostAddress.indexOf('%')
                                return if (index < 0)
                                    hostAddress.toUpperCase()
                                else
                                    hostAddress.substring(0, index).toUpperCase()
                            }
                        }
                    }
                }
            }
        } catch (e: SocketException) {
            e.printStackTrace()
        }

        return null
    }

}