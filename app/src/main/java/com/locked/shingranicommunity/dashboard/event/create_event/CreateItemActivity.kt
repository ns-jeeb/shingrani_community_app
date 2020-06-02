package com.locked.shingranicommunity.dashboard.event.create_event

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.databinding.DataBindingUtil
import com.locked.shingranicommunity.R
import com.locked.shingranicommunity.dashboard.DashBoardViewModel
import com.locked.shingranicommunity.databinding.CreateItemActivityBinding
import com.locked.shingranicommunity.di.DashboardComponent
import com.locked.shingranicommunity.MyApplication
import javax.inject.Inject

class CreateItemActivity : AppCompatActivity() {

    lateinit var mBinding: CreateItemActivityBinding
    @Inject
    lateinit var  dashBoardViewModel: DashBoardViewModel
    @Inject
    lateinit var dashboardCompunent : DashboardComponent

    override fun onCreate(savedInstanceState: Bundle?) {
        dashboardCompunent = (application as MyApplication).appComponent.dashBoardComponent().create()
        dashboardCompunent.inject(this)
        super.onCreate(savedInstanceState)

        mBinding = DataBindingUtil.setContentView(this,R.layout.create_item_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, CreateEventFragment())
                .commitNow()
        }
        mBinding.backPress.setOnClickListener() {
            onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.member_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.action_back){
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }
}
