package com.locked.shingranicommunity.auth

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.locked.shingranicommunity.MyApplication
import com.locked.shingranicommunity.R
import com.locked.shingranicommunity.TestClass
import com.locked.shingranicommunity.common.BaseActivity
import com.locked.shingranicommunity.databinding.ActivityRegistrationBinding
import com.locked.shingranicommunity.di2.auth.AuthComponent
import com.locked.shingranicommunity.common.NavigationHandler
import javax.inject.Inject

class AuthActivity : BaseActivity(), AuthComponentProvider {

    @Inject
    lateinit var testClass: TestClass

    lateinit var binding: ActivityRegistrationBinding
    override lateinit var authComponent: AuthComponent

    override fun onCreate(savedInstanceState: Bundle?) {
        authComponent = MyApplication.instance.appComponent2.authComponentFactory.create(this);
        authComponent.inject(this)
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_registration)
        loadByNavigation()
    }

    override fun onActivityActionMain() {
        intent.let {
            intent.putExtra(NavigationHandler.EXTRA_FRAGMENT_CLASS, LoginFragment::class.java)
        }
    }
}