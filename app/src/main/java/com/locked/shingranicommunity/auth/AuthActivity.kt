package com.locked.shingranicommunity.auth

import android.os.Bundle
import android.view.View
import com.locked.shingranicommunity.TestClass
import com.locked.shingranicommunity.UrCommunityApplication
import com.locked.shingranicommunity.common.FragmentActivity
import com.locked.shingranicommunity.common.NavigationHandler
import com.locked.shingranicommunity.di.auth.AuthComponent
import javax.inject.Inject

class AuthActivity : FragmentActivity(), AuthComponentProvider {

    @Inject
    lateinit var testClass: TestClass

    override lateinit var authComponent: AuthComponent

    override fun onCreate(savedInstanceState: Bundle?) {
        authComponent = UrCommunityApplication.instance.appComponent.authComponentFactory.create(this);
        authComponent.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun loadToolbar() {
        binding.toolbar.visibility = View.GONE
    }

    override fun onActivityActionMain() {
        intent.let {
            intent.putExtra(NavigationHandler.EXTRA_FRAGMENT_CLASS, LoginFragment::class.java)
        }
    }
}