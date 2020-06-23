package com.locked.shingranicommunity.member

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject

class InviteMemberFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var viewModel: InviteViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setupViews()
        return View(context)
    }

    private fun setupViews() {}

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity as MemberComponentProvider).memberComponent.inject(this)
        viewModel = ViewModelProvider(this, viewModelFactory).get(InviteViewModel::class.java)
    }
}