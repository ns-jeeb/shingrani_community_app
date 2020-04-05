package com.locked.shingranicommunity.dashboard.announncement.create_announce

import android.app.Activity
import android.content.Intent
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
import com.locked.shingranicommunity.Constant_Utils.ANNOUNCEMENT_CREATED
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
            if (announceViewModel.titleValidation(mBinding.edCreateAnnouncementTitle) && announceViewModel.detailsValidation(mBinding.edCreateAnnouncementDetail)) {
                announceViewModel.createAnnouncement(mBinding.edCreateAnnouncementTitle.text.toString(),mBinding.edCreateAnnouncementDetail.text.toString()).observe(this, Observer {
                    if (it != null){
                        Log.d(CreateAnnouncementActivity::class.java.name,"${it.fields?.get(0)?.name}")
                        var intent = Intent()
                        intent.putExtra(ANNOUNCEMENT_CREATED,true)
                        setResult(Activity.RESULT_OK,intent)
                        finish()
                    }
                })
            }

        }

    }
}
