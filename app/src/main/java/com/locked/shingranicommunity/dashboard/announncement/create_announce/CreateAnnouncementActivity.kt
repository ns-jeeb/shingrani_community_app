package com.locked.shingranicommunity.dashboard.announncement.create_announce

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.locked.shingranicommunity.Constant_Utils.ANNOUNCEMENT_CREATED
import com.locked.shingranicommunity.R
import com.locked.shingranicommunity.di2.viewmodel.ViewModelProviderFactory
import com.locked.shingranicommunity.databinding.ActivityCreateAnnouncementBinding
import com.locked.shingranicommunity.MyApplication
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
                    if (it.isNullOrBlank()){
                        var intent = Intent()
                        intent.putExtra(ANNOUNCEMENT_CREATED,true)
                        setResult(Activity.RESULT_OK,intent)
                        finish()
                    }else{
                        mBinding.edCreateAnnouncementTitle.error = it
                    }
                })
            }

        }

    }
}
