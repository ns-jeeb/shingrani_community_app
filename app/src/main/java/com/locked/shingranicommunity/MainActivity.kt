package com.locked.shingranicommunity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.locked.shingranicommunity.databinding.ActivityMainBinding
import com.locked.shingranicommunity.databinding.FragmentUserProfileBinding
import com.locked.shingranicommunity.tutorials.UserProfileFragment

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.setLifecycleOwner(this)
        binding.txtClick.setOnClickListener(View.OnClickListener {

            val fragmentManager = supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            val fragment = UserProfileFragment()
            fragmentTransaction.add(R.id.container, fragment)
            fragmentTransaction.commit()
        })
    }
}
