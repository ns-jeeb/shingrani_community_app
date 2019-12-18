
package com.locked.shingranicommunity.registration_login.registration

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.locked.shingranicommunity.MainActivity
import com.locked.shingranicommunity.R
import com.locked.shingranicommunity.di.RegisterComponent
import com.locked.shingranicommunity.registration_login.registration.enterdetails.EnterDetailsFragment
import javax.inject.Inject

class RegistrationActivity : AppCompatActivity() {

    @Inject
    lateinit var registrationViewModel: RegistrationViewModel
    @Inject
    lateinit var registerComponent: RegisterComponent

    override fun onCreate(savedInstanceState: Bundle?) {
        registerComponent = (application as MyApplication).appComponent.registerComponent().create()
        registerComponent.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)
        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_holder, EnterDetailsFragment())
            .commit()
    }


    /**
     * Callback from T&CsFragment when TCs have been accepted
     */
    fun onTermsAndConditionsAccepted() {
        registrationViewModel.registerUser()
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
        } else {
            super.onBackPressed()
        }
    }
}
