package com.locked.shingranicommunity.dashboard

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.viewpager.widget.ViewPager
import com.locked.shingranicommunity.CommunityApp
import com.viewpagerindicator.TitlePageIndicator
import com.locked.shingranicommunity.R
import com.locked.shingranicommunity.dashboard.announncement.AnnounceFragment
import com.locked.shingranicommunity.dashboard.event.EventFragment
import com.locked.shingranicommunity.databinding.ActivityDashBoradViewPagerBinding

class DashBoardViewPagerActivity : AppCompatActivity(), EventFragment.OnEventFragmentTransaction {
    override fun onFragmentInteraction(uri: Uri) {

    }

    private lateinit var mBinding : ActivityDashBoradViewPagerBinding
    private lateinit var tabs: TitlePageIndicator
    private lateinit var pager: ViewPager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_dash_borad_view_pager)
        initViews()
        setpuViewPager()

    }

    private fun initViews() {
        tabs = mBinding.tabs
        pager = mBinding.dashBorderPager
    }

    fun setpuViewPager(){

        val adapter = DashboardAdapter(supportFragmentManager)

        var eventFragment = EventFragment.newInstance("","")
        var announcementFragment = AnnounceFragment.newInstance()

        val density = resources.displayMetrics.density
        tabs.setBackgroundColor(CommunityApp.instance.getColor(R.color.colorPrimary))
        tabs.isSelectedBold = true
        tabs.footerColor = CommunityApp.instance.getColor(R.color.colorAccent)
        tabs.setOnPageChangeListener(object : ViewPager.OnPageChangeListener{
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
//                Toast.makeText(CommunityApp.instance,"Tab Scrolled",Toast.LENGTH_LONG).show()

            }

            override fun onPageSelected(position: Int) {
//                Toast.makeText(CommunityApp.instance,"Tab Selected",Toast.LENGTH_LONG).show()
            }

            override fun onPageScrollStateChanged(state: Int) {
//                Toast.makeText(CommunityApp.instance,"Tab Page Selected",Toast.LENGTH_LONG).show()
            }

        })
        adapter.addFragment(eventFragment,"Event")
        adapter.addFragment(announcementFragment, "Announcement")
        pager.adapter = adapter
        tabs.setViewPager(pager)

    }
}
