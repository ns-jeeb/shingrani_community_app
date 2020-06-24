package com.locked.shingranicommunity.common

import android.os.Bundle
import android.view.View
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

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        loadToolbar()
    }

    private fun loadToolbar() {
        val fragment = supportFragmentManager.findFragmentById(R.id.fragment)
        if (fragment != null && fragment is ToolbarProvider) {
            binding.toolbar.visibility = View.GONE
            setSupportActionBar(fragment.provideToolbar())
        } else {
            binding.toolbar.visibility = View.VISIBLE
            setSupportActionBar(binding.toolbar)
        }
    }
}