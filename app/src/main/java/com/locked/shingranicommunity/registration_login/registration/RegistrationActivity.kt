
package com.locked.shingranicommunity.registration_login.registration

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.locked.shingranicommunity.MyApplication
import com.locked.shingranicommunity.R
import com.locked.shingranicommunity.databinding.ActivityRegistrationBinding
import com.locked.shingranicommunity.di.RegisterComponent
import com.locked.shingranicommunity.registration_login.registration.enterdetails.RegisterFragment
import javax.inject.Inject

class RegistrationActivity : AppCompatActivity() {

    @Inject
    lateinit var registrationViewModel: RegistrationViewModel
    @Inject
    lateinit var registerComponent: RegisterComponent

    lateinit var binding : ActivityRegistrationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        registerComponent = (application as MyApplication).appComponent.registerComponent().create()
        registerComponent.inject(this)
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_registration)
        supportFragmentManager.beginTransaction()
            .add(R.id.fragment, RegisterFragment())
            .commit()
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
        } else {
            super.onBackPressed()
        }
    }
}
