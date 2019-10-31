package com.locked.shingranicommunity.authenticate.register

import android.content.Intent
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil

import com.locked.shingranicommunity.R
import com.locked.shingranicommunity.authenticate.ui.login.LoginActivity
import com.locked.shingranicommunity.databinding.RegisterationActivityBinding

class RegistrationActivity : AppCompatActivity(),View.OnClickListener {
    override fun onClick(v: View?) {
        var intent: Intent = Intent(this, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        this.finish()
    }

    companion object {
        fun newInstance() = RegistrationActivity()
    }

    private lateinit var viewModel: RegisterationViewModel
    private lateinit var  mBinding: RegisterationActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this,R.layout.registeration_activity)
        mBinding.btnJoiningPermission.setOnClickListener(this)
    }

    override fun onStart() {
        super.onStart()
        viewModel = ViewModelProviders.of(this).get(RegisterationViewModel::class.java)
    }

}
