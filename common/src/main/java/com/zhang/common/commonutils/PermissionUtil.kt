package com.zhang.common.commonutils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import com.zhang.common.R
import com.zhang.common.baseapp.AppManager

/**
 * Created by zhang .
 * DATA: 2018/7/26 .
 * Description : 权限申请工具类
 */
object PermissionUtil {

    // 权限code
    val PERMS_APPLY_CODE = 110
    private var alertDialog: AlertDialog? = null

    /**
     * 检查权限申请结果
     */
    fun isPermissionOk(grantResults: IntArray): Boolean {
        var isOk = true
        for (i in grantResults.indices) {
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) { //含有未申请到的权限
                isOk = false
                break
            }
        }
        return isOk
    }

    /**
     * 检查敏感权限
     *
     * @param permissions 权限
     * @return 结果
     */
    fun checkPermissions(context: Context, permissions: Array<out String>): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true
        }
        for (permission in permissions) {
            if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }

    /**
     * 动态申请权限
     */
    fun requestPermission(context: Context, permissions: Array<out String>) {
        var isCanShowApplyAgain = false
        for (i in permissions.indices) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(context as Activity, permissions[i])) {
                isCanShowApplyAgain = true
            }
        }
        if (isCanShowApplyAgain) { //可以继续弹出权限申请
            showPermissionsDialog(context)
        } else {
            ActivityCompat.requestPermissions(context as Activity, permissions, PERMS_APPLY_CODE)
        }
    }

    /**
     * 再次申请权限对话框
     *
     * @param permissions   权限
     * @param resultCode    权限申请码
     */
    fun showPermissionsDialog(context: Context) {
        alertDialog = AlertDialog.Builder(context)
                .setTitle(context.getString(R.string.permission_tip))
                .setMessage(context.getString(R.string.permission_tip_message))
                .setNegativeButton(context.getString(R.string.cancel)) { dialogInterface, i -> AppManager.instance.finishActivity() }
                .setPositiveButton(context.getString(R.string.ok), null)
                .setCancelable(false)
                .create()
        alertDialog!!.show()
        // 以下方式可以让alertDialog点击时不自动消失
        alertDialog!!.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
            val intent = Intent()
            //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
            intent.data = Uri.fromParts("package", context.packageName, null)
            (context as Activity).startActivityForResult(intent, PERMS_APPLY_CODE)
        }
    }

    /**
     * 隐藏申请权限对话框
     */
    fun hidePermissionsDialog() {
        alertDialog?.cancel()
    }

}