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
import com.viewpagerindicator.TitlePageIndicator
import com.locked.shingranicommunity.R
import com.locked.shingranicommunity.dashboard.announncement.AnnounceFragment
import com.locked.shingranicommunity.dashboard.event.create_event.CreateItemActivity
import com.locked.shingranicommunity.dashboard.event.fetch_event.EventListFragment
import com.locked.shingranicommunity.databinding.ActivityDashBoradViewPagerBinding
import com.locked.shingranicommunity.di.DashboardComponent
import com.locked.shingranicommunity.members.MemberActivity
import com.locked.shingranicommunity.registration_login.registration.MyApplication
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
        setpuViewPager(this.token)
//        if (/*admin toke*/ !this.token.isBlank()){
//            mBinding.visibility = View.VISIBLE
//            mBinding.imgCreateItem.setOnClickListener(this)
//        }else{
//            mBinding.imgCreateItem.visibility = View.VISIBLE
//        }
//        mBinding.imgProfile.setOnClickListener(this)
//
//        mBinding.txtAnnouncement.text = intent.getStringExtra("USER_NAME")

    }

    private fun initViews() {
        tabs = mBinding.tabs
        pager = mBinding.dashBorderPager
    }

    override fun onStart() {
        super.onStart()
    }

    fun setpuViewPager(token: String){

        val adapter = DashboardPagerAdapter(supportFragmentManager)

        var eventFragment = EventListFragment.newInstance(token,false)
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
    // run only with admin token
    fun createItem(adminToken: String){
        var inten = Intent(this, CreateItemActivity::class.java)
        var bundle = Bundle()
        bundle.putString("token",adminToken)
        inten.putExtras(bundle)
        inten.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(inten)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100) {
            Toast.makeText(this,"Item is created ${data?.getStringExtra("response_message")}",Toast.LENGTH_LONG).show()
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
        if (id == R.id.action_logout){
            getSharedPreferences("token", Context.MODE_PRIVATE).edit().putString("token", "").apply()
            var intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
            return true
        }
        if (id == R.id.action_members) {
            var intent = Intent(this, MemberActivity::class.java)
            intent.putExtra("create_event",false)
            startActivity(intent)
            return true
        }
        if (id == R.id.action_add) {
            createItem(dashBoardViewModel.getToken())
        }
        return super.onOptionsItemSelected(item)
    }
}
