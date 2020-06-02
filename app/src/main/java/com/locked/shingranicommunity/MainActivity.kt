package com.locked.shingranicommunity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.locked.shingranicommunity.databinding.ActivityLoginBinding
import com.locked.shingranicommunity.registration_login.registration.RegistrationActivity
import com.locked.shingranicommunity.registration_login.registration.login.LoginActivity
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var mainViewModel: MainViewModel

    lateinit var binding: ActivityLoginBinding
    @SuppressLint("ObsoleteSdkInt")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
//        binding.setLifecycleOwner(this)


        val userManager = (application as MyApplication).appComponent.userManager()
        if (!userManager.isUserLoggedIn()) {
            if (!userManager.isUserRegistered()) {
                startActivity(Intent(this, RegistrationActivity::class.java))

            } else {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        } else {
            setContentView(R.layout.activity_login)
//            userManager.userComponent!!.inject(this)
            setupViews()
        }



    }

    override fun onResume() {
        super.onResume()
    }

    private fun setupViews() {
    }
}
