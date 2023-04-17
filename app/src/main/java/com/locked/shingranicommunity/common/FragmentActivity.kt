package com.locked.shingranicommunity.common

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import com.locked.shingranicommunity.R
import com.locked.shingranicommunity.databinding.ActivityFragmentBinding

open class FragmentActivity : BaseActivity() {

    lateinit var binding: ActivityFragmentBinding
    lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadLayoutBinding()
        loadByNavigation()
    }

    open fun loadLayoutBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_fragment)
        toolbar = binding.toolbar
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        loadToolbar()
    }

    open fun loadToolbar() {
        val fragment = supportFragmentManager.findFragmentById(R.id.fragment)
        if (fragment != null && fragment is ToolbarProvider) {
            toolbar.visibility = View.GONE
            setSupportActionBar(fragment.provideToolbar())
        } else {
            toolbar.visibility = View.VISIBLE
            setSupportActionBar(toolbar)
        }
    }
}