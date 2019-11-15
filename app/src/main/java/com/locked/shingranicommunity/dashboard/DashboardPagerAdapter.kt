package com.locked.shingranicommunity.dashboard

import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.PagerAdapter

import com.locked.shingranicommunity.CommunityApp

import com.locked.shingranicommunity.R


class DashboardPagerAdapter(manager: FragmentManager) : FragmentPagerAdapter (manager,BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment {
        return mFragmentList[position]
    }

    private val mFragmentList: ArrayList<Fragment> = ArrayList()
    private val mFragmentTitleList: ArrayList<String> = ArrayList()

    override fun getCount(): Int {
        return mFragmentList.size
    }
    override fun getPageTitle(position: Int): CharSequence {
        return mFragmentTitleList[position]
    }


    fun addFragment(fragment: Fragment, title: String) {
        mFragmentList.add(fragment)
        mFragmentTitleList.add(title)

    }
}