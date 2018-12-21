package com.zhang.common.baseapp

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import java.util.*

/**
 * Created by zhang .
 * DATA: 2018/7/21 .
 * Description : activity管理
 */
class AppManager private constructor(){

    var activityStack: Stack<Activity>? = null

    companion object {
        val instance by lazy(LazyThreadSafetyMode.SYNCHRONIZED){
            AppManager()
        }

    }

    /**
     * 添加Activity到堆栈
     */
    fun addActivity(activity: Activity) {
        if (activityStack == null) {
            activityStack = Stack()
        }
        activityStack?.add(activity)
    }

    /**
     * 移除Activity
     */
    fun removeActivity(activity: Activity) {
        if (activityStack == null) {
            activityStack = Stack()
        }
        activityStack?.remove(activity)
    }

    /**
     * 获取当前Activity(堆栈中最后一个压入栈的)
     */
    fun currentActivity(): Activity? {
        val activity: Activity? = activityStack?.lastElement()
        return activity
    }

    /**
     * 获取当前Activity的前一个Activity
     */
    fun preActivity(): Activity? {
        val index = activityStack?.size ?: 0-2
        if (index < 0) return null
        else return activityStack?.get(index)
    }

    /**
     * 关闭当前Activity
     */
    fun finishActivity() {
        val activity = activityStack?.lastElement()
        finishActivity(activity)

    }

    /**
     * 结束指定的Activity
     */
    fun finishActivity(targetActivity: Activity?) {
        var activity = targetActivity
        if (activity != null) {
            activityStack!!.remove(activity)
            activity.finish()
            activity = null
        }
    }


    /**
     * 结束指定类名的Activity
     */
    fun finishActivity(cls: Class<*>) {
        try {
            for (activity in activityStack!!) {
                if (cls == activity.javaClass) {
                    activity.finish()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 关闭所有Activity
     */
    fun finishAllActivity(){
        for (activity in activityStack!!){
            activity?.finish()
        }
        activityStack?.clear()
    }

    /**
     * 返回到指定的Activity
     */
    fun returnToActivity(cls: Class<*>){
        while (activityStack?.size != 0){
            if (cls == activityStack?.peek()?.javaClass){
                break
            } else {
                finishActivity(activityStack?.peek())
            }
        }
    }

    /**
     * 是否已经打开指定的Activity
     */
    fun isOpenActivity(cls: Class<*>):Boolean{
        if (activityStack != null){
            for (activity in activityStack!!){
                if (cls == activity.javaClass){
                    return true
                }
            }
        }
        return false
    }

     @SuppressLint("ServiceCast")
             /**
     * 退出应用程序
     * @param context      上下文
     * @param isBackground 是否开开启后台运行
     */
    fun appExit(context:Context,isBackground:Boolean){
        try {
            finishAllActivity()
            val activityMgr:ActivityManager = context.getSystemService(Context.ACCOUNT_SERVICE) as ActivityManager
            activityMgr.killBackgroundProcesses(context.packageName)
        }catch (e:Exception){

        }finally {
            if (!isBackground){
                System.exit(0)
            }
        }
    }

}