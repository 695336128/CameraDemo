//package com.zhang.common.test
//
//import com.zhang.common.baserx.RxHelper
//import com.zhang.common.baserx.RxSchedulers
//import com.zhang.common.retrofit.RetrofitUtils
//import io.reactivex.Observable
//
///**
// * Created by zhang .
// * DATA: 2018/7/27 .
// * Description :
// */
//class TestModel: TestContract.Model{
//    override fun loadData(): Observable<TestBean> {
//        // 使用RxJava+Retrofit进行网络请求
//        val ipService = RetrofitUtils.instance.creat(Api::class.java)
//        return ipService.getTest()
//                .compose(RxSchedulers.io_main())
//                .compose(RxHelper.handleResult())
//    }
//}
//
