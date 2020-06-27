package com.locked.shingranicommunity.dashboard

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import com.locked.shingranicommunity.R
import com.locked.shingranicommunity.UrCommunityApplication
import com.locked.shingranicommunity.announcement.AnnouncementComponentProvider
import com.locked.shingranicommunity.announcement.AnnouncementListFragment
import com.locked.shingranicommunity.databinding.ActivityDashboardBinding
import com.locked.shingranicommunity.di.announcement.AnnouncementComponent
import com.locked.shingranicommunity.di.dashboard.DashboardComponent
import com.locked.shingranicommunity.di.event.EventComponent
import com.locked.shingranicommunity.event.EventComponentProvider
import com.locked.shingranicommunity.event.EventListFragment
import javax.inject.Inject

class DashboardActivity : AppCompatActivity(),
    EventComponentProvider,
    AnnouncementComponentProvider,
    View.OnClickListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    lateinit var dashboardComponent: DashboardComponent
    override lateinit var eventComponent: EventComponent
    override lateinit var announcementComponent: AnnouncementComponent

    private lateinit var viewModel: DashboardViewModel
    private lateinit var binding : ActivityDashboardBinding
    private lateinit var adapter: DashboardPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        dashboardComponent = UrCommunityApplication.instance.appComponent.dashboardComponentFactory.create(this)
        dashboardComponent.inject(this)
        viewModel = ViewModelProvider(this, viewModelFactory).get(DashboardViewModel::class.java)
        eventComponent = UrCommunityApplication.instance.appComponent.eventComponentFactory.create(this)
        announcementComponent = UrCommunityApplication.instance.appComponent.announcementComponentFactory.create(this)
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_dashboard)
        adapter = DashboardPagerAdapter(supportFragmentManager)
        setupViews()
    }

    private fun setupViews() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = ""
        setupViewPager()
        initFab()
    }

    private fun setupViewPager(){
        val eventFragment = EventListFragment()
        val announcementFragment = AnnouncementListFragment()
        adapter.addFragment(eventFragment, getString(R.string.event_tab))
        adapter.addFragment(announcementFragment, getString(R.string.announcement_tab))
        binding.pager.adapter = adapter
        binding.tabLayout.setupWithViewPager(binding.pager)
        binding.pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {}
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
            override fun onPageSelected(position: Int) {
                when (viewModel.currPage.value) {
                    DashboardViewModel.PAGE_EVENTS -> if (position == 1) viewModel.pageChanged(DashboardViewModel.PAGE_ANNOUNCEMENTS)
                    DashboardViewModel.PAGE_ANNOUNCEMENTS -> if (position == 0) viewModel.pageChanged(DashboardViewModel.PAGE_EVENTS)
                }
            }
        })
        viewModel.currPage.observe(this, Observer {
            it?.let {
                when (it) {
                    DashboardViewModel.PAGE_EVENTS -> binding.pager.setCurrentItem(0, true)
                    DashboardViewModel.PAGE_ANNOUNCEMENTS -> binding.pager.setCurrentItem(1, true)
                }
            }
        })
    }

    private fun initFab() {
        viewModel.showCreateFab.observe(this, Observer {
            if (it) {
                binding.fab.visibility = View.VISIBLE
            } else {
                binding.fab.visibility = View.GONE
            }
        })
        binding.fab.setOnClickListener(this)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.menu_dashboard,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId) {
            R.id.action_members -> {
                viewModel.memberPressed()
                return true
            }
            R.id.action_settings -> {
                viewModel.settingsPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onClick(view: View?) {
        if (view == binding.fab) {
            viewModel.fabPressed()
        }
    }
}