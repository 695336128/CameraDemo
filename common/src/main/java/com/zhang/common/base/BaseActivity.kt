package com.zhang.common.base

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.app.AppCompatDelegate
import android.view.Window
import butterknife.ButterKnife
import butterknife.Unbinder
import com.zhang.common.baseapp.AppManager
import com.zhang.common.baserx.RxManager
import com.zhang.common.commonutils.LoadingUtil
import com.zhang.common.commonutils.TUtil
import com.zhang.common.commonutils.ToastUtil

/**
 * Created by zhang .
 * DATA: 2018/7/22 .
 * Description : BASE ACTIVITY
 */
abstract class BaseActivity<T : BasePresenter<*, *>, E : BaseModel> : AppCompatActivity() {
    var mPresenter: T? = null
    var mModel: E? = null
    var mContext: Context? = null
    var unbinder: Unbinder? = null
    var mRxManager: RxManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mRxManager = RxManager()
        doBeforeSetcontentView()
        setContentView(getLayoutId())
        unbinder = ButterKnife.bind(this)
        mContext = this
        mPresenter = TUtil.getT<T>(this, 0)
        mModel = TUtil.getT(this, 1)
        mPresenter?.mContext = this
        this.initPresenter()
        this.initView()
    }

    /**
     * 设置layout前配置
     */
    fun doBeforeSetcontentView() {
        // 把Activity放到application栈中管理
        AppManager.instance.addActivity(this)
        // 无标题设置
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        // 设置横屏(or 竖屏)
//        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        // TODO: 默认着色状态栏
    }

    /**************** 子类实现 *****************/
    //获取布局文件
    abstract fun getLayoutId(): Int

    //简单页面无需mvp就不用管此方法即可,完美兼容各种实际场景的变通
    abstract fun initPresenter()

    //初始化view
    abstract fun initView()

    /**
     * 通过Class跳转页面
     */
    fun startActivity(cls: Class<*>) {
        startActivity(cls, null)
    }

    fun startActivityForResult(cls: Class<*>, requestCode: Int) {
        startActivityForResult(cls, null, requestCode)
    }

    /**
     * 含有Bundle和Result跳转页面
     */
    fun startActivityForResult(cls: Class<*>, bundle: Bundle?, requestCode: Int) {
        val intent = Intent()
        intent.setClass(this, cls)
        if (bundle != null) {
            intent.putExtras(bundle)
        }
        startActivityForResult(intent, requestCode)
    }

    /**
     * 含有Bundle通过Class跳转页面
     */
    fun startActivity(cls: Class<*>, bundle: Bundle?) {
        val intent = Intent()
        intent.setClass(this, cls)
        if (bundle != null) {
            intent.putExtras(bundle)
        }
        startActivity(intent)
    }

    /**
     * 设置昼夜主题
     */
    fun initTheme(@AppCompatDelegate.NightMode mode: Int){
        delegate.setLocalNightMode(mode)
        recreate()
    }

    /**
     * 开启浮动加载进度条
     */
    fun startProgressDialog() {
        LoadingUtil.showLoading(this)
    }

    /**
     * 停止浮动加载进度条
     */
    fun stopProgressDialog() {
        LoadingUtil.hideLoading()
    }

    /**
     * 显示Toast提示(来自String)
     */
    fun showToast(text: String) {
        ToastUtil.showToast(this,text)
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mPresenter != null) {
            mPresenter?.onDestroy()
        }
        AppManager.instance.removeActivity(this)
        unbinder?.unbind()
    }


}