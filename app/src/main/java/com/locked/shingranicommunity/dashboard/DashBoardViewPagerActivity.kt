package com.locked.shingranicommunity.dashboard

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.locked.shingranicommunity.Constant_Utils
import com.locked.shingranicommunity.Constant_Utils.ONE_00
import com.locked.shingranicommunity.Constant_Utils.ONE_02
import com.locked.shingranicommunity.R
import com.locked.shingranicommunity.dashboard.announncement.AnnounceFragment
import com.locked.shingranicommunity.dashboard.announncement.create_announce.CreateAnnouncementActivity
import com.locked.shingranicommunity.dashboard.event.create_event.CreateItemActivity
import com.locked.shingranicommunity.dashboard.event.fetch_event.EventListFragment
import com.locked.shingranicommunity.databinding.ActivityDashBoradViewPagerBinding
import com.locked.shingranicommunity.di.DashboardComponent
import com.locked.shingranicommunity.members.MemberActivity
import com.locked.shingranicommunity.registration_login.registration.MyApplication
import com.locked.shingranicommunity.settings.SettingsActivity
import javax.inject.Inject

@Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS", "NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class DashBoardViewPagerActivity : AppCompatActivity(), View.OnClickListener {

    @Inject
    lateinit var  dashboardViewModel: DashBoardViewModel

    @Inject
    lateinit var dashboardComponent : DashboardComponent

    private lateinit var adapter: DashboardPagerAdapter
    private lateinit var binding : ActivityDashBoradViewPagerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        dashboardComponent = (application as MyApplication).appComponent.dashBoardComponent().create()
        dashboardComponent.inject(this)
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_dash_borad_view_pager)
        adapter = DashboardPagerAdapter(supportFragmentManager)
        initViews()
    }

    private fun initViews() {
        setSupportActionBar(binding.toolbar)
        setupViewPager()
        initFab()
    }

    private fun setupViewPager(){
        val eventFragment = EventListFragment.newInstance(false)
        val announcementFragment = AnnounceFragment.newInstance()
        adapter.addFragment(eventFragment, getString(R.string.event_tab))
        adapter.addFragment(announcementFragment, getString(R.string.announcement_tab))
        binding.pager.adapter = adapter
        binding.tabLayout.setupWithViewPager(binding.pager)
    }

    private fun initFab() {
        dashboardViewModel.showCreateFab.observe(this, Observer {
            if (it) {
                binding.fab.visibility = View.VISIBLE
            } else {
                binding.fab.visibility = View.GONE
            }
        })
        binding.fab.setOnClickListener(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null){
            if (requestCode == ONE_00) {
                Toast.makeText(this,"Item is created ${data?.getStringExtra("response_message")}",Toast.LENGTH_LONG).show()
                dashboardViewModel.loadItem(this)
                setupViewPager()
            } else if (requestCode == ONE_02){
                finish()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.menu_dashboard,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId) {
            R.id.action_members -> {
                startActivity(Intent(this, MemberActivity::class.java))
                return true
            }
            R.id.action_settings -> {
                startActivity(Intent(this, SettingsActivity::class.java))
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onClick(view: View?) {
        if (view == binding.fab) {
            create()
        }
    }

    private fun create() {
        if (binding.pager.currentItem == 0) {
            val intent = Intent(this, CreateItemActivity::class.java)
            startActivityForResult(intent, Constant_Utils.ONE_00)
        } else if (binding.pager.currentItem == 1) {
            val intent = Intent(this, CreateAnnouncementActivity::class.java)
            startActivityForResult(intent, 102)
        }
    }
}