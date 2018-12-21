package com.zhang.common.base

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.zhang.common.commonutils.CollectionUtils

/**
 * Created by zhang .
 * DATA: 2018/7/26 .
 * Description :
 * FragmentStatePagerAdapter 和前面的 FragmentPagerAdapter 一样，是继承子 PagerAdapter。
 * 但和 FragmentPagerAdapter 不一样的是，
 * 正如其类名中的 'State' 所表明的含义一样，
 * 该 PagerAdapter 的实现将只保留当前页面，
 * 当页面离开视线后，就会被消除，释放其资源；
 * 而在页面需要显示时，生成新的页面(就像 ListView 的实现一样)。
 * 这么实现的好处就是当拥有大量的页面时，不必在内存中占用大量的内存。
 */
class BaseFragmentStateAdapter : FragmentStatePagerAdapter{

    var fm: FragmentManager? = null
    var fragmentList : List<Fragment>? = null
    var mTitles: List<String>? = null

    constructor(fm: FragmentManager):super(fm){
        this.fm = fm
    }

    constructor(fm: FragmentManager,fragmentList : List<Fragment>):super(fm){
        this.fm = fm
        this.fragmentList = fragmentList
    }

    constructor(fm: FragmentManager,fragmentList : List<Fragment>,mTitles: List<String>):super(fm){
        this.fm = fm
        this.fragmentList = fragmentList
        this.mTitles = mTitles
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