//package com.zhang.common.test
//
//import android.annotation.SuppressLint
//import android.os.Bundle
//import android.support.v7.app.AppCompatDelegate
//import android.view.View
//import android.widget.Button
//import android.widget.LinearLayout
//import android.widget.TextView
//import android.widget.Toast
//import butterknife.BindView
//import butterknife.OnClick
//import com.zhang.common.R
//import com.zhang.common.base.BaseActivity
//import com.zhang.common.commonutils.L
//import com.zhang.common.retrofit.RetrofitUtils
//
///**
// * Created by zhang .
// * DATA: 2018/7/26 .
// * Description :
// */
//class TestActivity: BaseActivity<TestPresenter, TestModel>(),TestContract.View{
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        RetrofitUtils.BASE_URL = "http://www.wanandroid.com/"
//    }
//
//    @BindView(R.id.root_ll)
//    lateinit var root_ll: LinearLayout
//
//    @BindView(R.id.result_tv)
//    lateinit var resultTv: TextView
//
//    @BindView(R.id.request_button)
//    lateinit var requestBtn: Button
//
//    @BindView(R.id.theme_button)
//    lateinit var themeBtn: Button
//
//    @BindView(R.id.jump_button)
//    lateinit var jumpBtn: Button
//
//
//    override fun getLayoutId(): Int {
//        return R.layout.activity_test
//    }
//
//    override fun initPresenter() {
//        mPresenter?.setVM(this,mModel!!)
//    }
//
//    override fun initView() {
//
//    }
//
//
//    @SuppressLint("SetTextI18n")
//    override fun doWithData(data: TestBean) {
//        resultTv.text = "name: ${data.name} \n url: ${data.url}"
//        L.d("name: ${data.name} \n url: ${data.url}")
//    }
//
//    override fun doWithEvent(data: TestBean) {
//        Toast.makeText(mContext,"GetEvevt",Toast.LENGTH_SHORT).show()
//    }
//
//    @OnClick(R.id.request_button,R.id.theme_button,R.id.jump_button)
//    fun onClick(view: View){
//        when(view.id){
//            R.id.request_button -> {mPresenter?.loadDataRequest()}
//            R.id.theme_button -> {initTheme(AppCompatDelegate.MODE_NIGHT_YES)}
//            R.id.jump_button -> {startActivity(TestAvtivity2::class.java)}
//            else -> {}
//        }
//    }
//
//    override fun showLoading() {
//        startProgressDialog()
//    }
//
//    override fun stopLoading() {
//        stopProgressDialog()
//    }
//
//    override fun showErrorTip(msg: String) {
//        showToast(msg)
//    }
//}