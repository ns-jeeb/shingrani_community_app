package com.locked.shingranicommunity.dashboard

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.viewpager.widget.ViewPager
import com.locked.shingranicommunity.Constant_Utils
import com.locked.shingranicommunity.Constant_Utils.ONE_00
import com.locked.shingranicommunity.Constant_Utils.ONE_01
import com.locked.shingranicommunity.Constant_Utils.ONE_02
import com.viewpagerindicator.TitlePageIndicator
import com.locked.shingranicommunity.R
import com.locked.shingranicommunity.dashboard.announncement.AnnounceFragment
import com.locked.shingranicommunity.dashboard.event.create_event.CreateItemActivity
import com.locked.shingranicommunity.dashboard.event.fetch_event.EventListFragment
import com.locked.shingranicommunity.databinding.ActivityDashBoradViewPagerBinding
import com.locked.shingranicommunity.di.DashboardComponent
import com.locked.shingranicommunity.members.MemberActivity
import com.locked.shingranicommunity.registration_login.registration.MyApplication
import com.locked.shingranicommunity.registration_login.registration.SettingsActivity
import com.locked.shingranicommunity.registration_login.registration.login.LoginActivity
import javax.inject.Inject

@Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS", "NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class DashBoardViewPagerActivity : AppCompatActivity(), EventListFragment.OnEventFragmentTransaction, View.OnClickListener {
    override fun onClick(v: View?) {
    }

    override fun onFragmentInteraction(uri: Uri) {
    }

    @Inject
    lateinit var  dashBoardViewModel: DashBoardViewModel
    @Inject
    lateinit var dashboardCompunent : DashboardComponent

    private lateinit var mBinding : ActivityDashBoradViewPagerBinding
    private lateinit var tabs: TitlePageIndicator
    private lateinit var pager: ViewPager
    var token = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        dashboardCompunent = (application as MyApplication).appComponent.dashBoardComponent().create()
        dashboardCompunent.inject(this)
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_dash_borad_view_pager)
        setSupportActionBar(mBinding.toolbar)
        var token = getSharedPreferences("token", Context.MODE_PRIVATE).getString("token","")
        this.token = token
        initViews()
        setpuViewPager()
    }

    private fun initViews() {
        tabs = mBinding.tabs
        pager = mBinding.dashBorderPager
    }

    override fun onStart() {
        super.onStart()
    }

    fun setpuViewPager(){

        val adapter = DashboardPagerAdapter(supportFragmentManager)

        var eventFragment = EventListFragment.newInstance(false)
        var announcementFragment = AnnounceFragment.newInstance()

        val density = resources.displayMetrics.density
        tabs.setBackgroundColor(MyApplication.instance.getColor(R.color.colorPrimary))

        tabs.isSelectedBold = true
        tabs.footerColor = MyApplication.instance.getColor(R.color.colorAccent)
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
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null){
            if (requestCode == ONE_00) {
                Toast.makeText(this,"Item is created ${data?.getStringExtra("response_message")}",Toast.LENGTH_LONG).show()
                dashBoardViewModel.loadItem(this)
                setpuViewPager()
            }else if (requestCode == ONE_02){
                finish()
            }
        }
    }

    fun hideOrShowProgress(showProgress: Boolean) {
        if (showProgress) {
            mBinding.loadingItemProgress.visibility = View.VISIBLE
            mBinding.txtLoadingItem.visibility = View.VISIBLE
        }else{
            mBinding.loadingItemProgress.visibility = View.GONE
            mBinding.txtLoadingItem.visibility = View.GONE
        }
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.menu_user_list,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        var id = item?.itemId
        when (id) {
            R.id.action_members -> {
                var intent = Intent(this, MemberActivity::class.java)
                intent.putExtra("create_event",false)
                startActivity(intent)
                return true
            }
            R.id.action_settings -> {
                var intent = Intent(this, SettingsActivity::class.java)
                startActivityForResult(intent,Constant_Utils.ONE_02)
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}