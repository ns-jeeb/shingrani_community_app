package com.locked.shingranicommunity.dashboard.announncement.create_announce

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.locked.shingranicommunity.R
import com.locked.shingranicommunity.ViewModelProviderFactory
import com.locked.shingranicommunity.databinding.ActivityCreateAnnouncementBinding
import com.locked.shingranicommunity.di.ViewModelProviderModul
import com.locked.shingranicommunity.registration_login.registration.MyApplication
import javax.inject.Inject

class CreateAnnouncementActivity : AppCompatActivity() {

    lateinit var mBinding: ActivityCreateAnnouncementBinding
    lateinit var announceViewModel: CreateAnnounceViewModel
    @Inject
    lateinit var viewModelProviders: ViewModelProviderFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as MyApplication).appComponent.dashBoardComponent().create().inject(this )
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_create_announcement)
        announceViewModel = ViewModelProviders.of(this,viewModelProviders).get(CreateAnnounceViewModel::class.java)
        mBinding.backPress.setOnClickListener{
            onBackPressed()
        }
    }
}
