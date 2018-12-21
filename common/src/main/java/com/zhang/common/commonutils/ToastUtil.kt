package com.zhang.common.commonutils

import android.content.Context
import android.widget.Toast
import java.lang.ref.WeakReference

/**
 * Created by zhang .
 * DATA: 2018/8/8 .
 * Description : Toast_Util
 */
object ToastUtil {

    private var mToast: Toast? = null

    /**
     * 显示Toast
     */
    fun showToast(context: Context, message: String) {
        cancel()
        val weakReference = WeakReference<Context>(context)
        mToast = Toast.makeText(weakReference.get(),message,Toast.LENGTH_SHORT)
        mToast?.show()
    }

    /**
     * 长时间显示Toast
     */
    fun showToastLong(context: Context, message: String) {
        cancel()
        val weakReference = WeakReference<Context>(context)
        mToast = Toast.makeText(weakReference.get(),message,Toast.LENGTH_LONG)
        mToast?.show()
    }

    fun cancel() {
        mToast?.cancel()
    }
}