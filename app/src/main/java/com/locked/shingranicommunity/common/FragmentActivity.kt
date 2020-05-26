package com.locked.shingranicommunity.common

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.locked.shingranicommunity.R
import com.locked.shingranicommunity.databinding.ActivityFragmentBinding

open class FragmentActivity : BaseActivity() {

    lateinit var binding: ActivityFragmentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_fragment)
        loadByNavigation()
    }
}