package com.zhang.common.commonutils

import android.content.Context
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import com.wang.avi.AVLoadingIndicatorView
import com.wang.avi.Indicator
import com.zhang.common.R
import java.lang.ref.WeakReference

/**
 * Created by zhang .
 * DATA: 2018/8/6 .
 * Description : 进度条加载工具
 */
object LoadingUtil {

    var dialog:AlertDialog? = null

    /**
     * 显示加载框
     */
    fun showLoading(context: Context){
        if (dialog != null && dialog!!.isShowing){
            return
        }
        val weakReference: WeakReference<Context> = WeakReference(context)
        dialog = AlertDialog.Builder(weakReference.get()!!,R.style.progressdialog)
                .setView(R.layout.dialog_loading)
                .setCancelable(false)
                .create()
        dialog?.setCanceledOnTouchOutside(false)
        dialog?.show()
    }

    fun showLoading(context: Context,indicator: Indicator){
        if (dialog != null && dialog!!.isShowing){
            return
        }
        val weakReference: WeakReference<Context> = WeakReference(context)
        val view: View = LayoutInflater.from(weakReference.get()).inflate(R.layout.dialog_loading,null)
        val avi: AVLoadingIndicatorView = view.findViewById(R.id.avi)
        avi.indicator = indicator
        dialog = AlertDialog.Builder(weakReference.get()!!,R.style.progressdialog)
                .setView(view)
                .setCancelable(false)
                .create()
        dialog?.setCanceledOnTouchOutside(false)
        dialog?.show()
    }

    fun hideLoading(){
        dialog?.cancel()
    }

}