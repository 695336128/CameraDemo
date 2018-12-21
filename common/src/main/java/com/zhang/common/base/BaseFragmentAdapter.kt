package com.zhang.common.base

import android.annotation.SuppressLint
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.app.FragmentTransaction
import com.zhang.common.commonutils.CollectionUtils

/**
 * Created by zhang .
 * DATA: 2018/7/26 .
 * Description : 该类内的每一个生成 Fragment 都将保存在内存之中
 * 因此适用那些相对静态的页面，数量也较少的
 * 如果需要处理很多页，并且数据动态性较大、内存占用较多的情况
 * 应该使用FragmentStatePagerAdapter
 */
class BaseFragmentAdapter : FragmentPagerAdapter {

    var fm: FragmentManager? = null
    var fragmentList: ArrayList<Fragment>? = null
    var mTitles: List<String>? = null

    constructor(fm: FragmentManager) : super(fm) {
        this.fm = fm
    }

    constructor(fm: FragmentManager, fragmentList: ArrayList<Fragment>) : super(fm) {
        this.fm = fm
        this.fragmentList = fragmentList
    }

    constructor(fm: FragmentManager, fragmentList: ArrayList<Fragment>, mTitles: List<String>) : super(fm) {
        this.fm = fm
        this.fragmentList = fragmentList
        this.mTitles = mTitles
    }

    /**
     * 设置Fragments
     */
    @SuppressLint("CommitTransaction")
    fun setFragments(fragmentList: ArrayList<Fragment>, mTitles: List<String>) {
        this.mTitles = mTitles
        val ft: FragmentTransaction? = fm?.beginTransaction()
        if (this.fragmentList != null) {
            for (f in this.fragmentList!!) {
                ft!!.remove(f)
            }
            ft!!.commitAllowingStateLoss()
            fm?.executePendingTransactions()
        }
        this.fragmentList = fragmentList
        notifyDataSetChanged()
    }

    override fun getPageTitle(position: Int): CharSequence {
        return if (CollectionUtils.isNullOrEmpty(mTitles)) "" else mTitles!![position]
    }


    override fun getItem(position: Int): Fragment? {
        return fragmentList?.get(position)
    }

    override fun getCount(): Int {
        return fragmentList?.size ?: 0
    }
}