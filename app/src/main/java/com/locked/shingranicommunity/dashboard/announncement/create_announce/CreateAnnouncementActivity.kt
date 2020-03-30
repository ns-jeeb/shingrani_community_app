package com.locked.shingranicommunity.dashboard.announncement.create_announce

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
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
        mBinding.btnCrAnnounce.setOnClickListener {
            announceViewModel.createAnnouncement(mBinding.edCreateAnnouncementTitle.text.toString(),mBinding.edCreateAnnouncementDetail.text.toString()).observe(this, Observer {
                if (it != null){
                    Toast.makeText(this,"${it.fields?.get(0)?.name}", Toast.LENGTH_LONG).show()
                    finish()
                    Log.d(CreateAnnouncementActivity::class.java.name,"${it.fields?.get(0)?.name}")

                }
            })
        }

    }
}
