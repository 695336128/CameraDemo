package com.zhang.common.base

/**
 * Created by zhang .
 * DATA: 2018/7/22 .
 * Description : BaseView
 */
interface BaseView {
    fun showLoading()
    fun stopLoading()
    fun showErrorTip(msg: String)
}