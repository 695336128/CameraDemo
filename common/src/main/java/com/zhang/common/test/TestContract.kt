//package com.zhang.common.test
//
//import com.zhang.common.base.BaseModel
//import com.zhang.common.base.BasePresenter
//import com.zhang.common.base.BaseView
//import io.reactivex.Observable
//
///**
// * Created by zhang .
// * DATA: 2018/7/26 .
// * Description :
// */
//interface TestContract {
//    interface Model : BaseModel {
//        fun loadData(): Observable<TestBean>
//    }
//
//    interface View : BaseView {
//        fun doWithData(data: TestBean)
//        fun doWithEvent(data: TestBean)
//    }
//
//    abstract class Presenter : BasePresenter<View, Model>() {
//        abstract fun loadDataRequest()
//    }
//
//
//}