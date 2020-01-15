
package com.locked.shingranicommunity.registration_login.registration

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.locked.shingranicommunity.MainActivity
import com.locked.shingranicommunity.R
import com.locked.shingranicommunity.dashboard.DashBoardViewPagerActivity
import com.locked.shingranicommunity.databinding.ActivityRegistrationBinding
import com.locked.shingranicommunity.di.RegisterComponent
import com.locked.shingranicommunity.di.ResponseEvent
import com.locked.shingranicommunity.mesages.DisplayResponseEvent
import com.locked.shingranicommunity.registration_login.registration.enterdetails.EnterDetailsFragment
import com.locked.shingranicommunity.registration_login.registration.login.LoginActivity
import com.locked.shingranicommunity.storage.StorageModule
import javax.inject.Inject

class RegistrationActivity : AppCompatActivity() {

    @Inject
    lateinit var registrationViewModel: RegistrationViewModel
    @Inject
    lateinit var registerComponent: RegisterComponent
    lateinit var mBinding : ActivityRegistrationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        registerComponent = (application as MyApplication).appComponent.registerComponent().create()
        registerComponent.inject(this)
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_registration)
        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_holder, EnterDetailsFragment())
            .commit()
    }


    /**
     * Callback from T&CsFragment when TCs have been accepted
     */
    fun onTermsAndConditionsAccepted() {
//        startActivity(Intent(this, LoginActivity::class.java))
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
        } else {
            super.onBackPressed()
        }
    }
}
