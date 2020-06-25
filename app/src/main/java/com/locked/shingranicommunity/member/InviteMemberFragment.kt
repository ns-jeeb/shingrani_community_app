package com.locked.shingranicommunity.member

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.widget.doOnTextChanged
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.locked.shingranicommunity.R
import com.locked.shingranicommunity.common.ViewUtil
import com.locked.shingranicommunity.databinding.FragmentMemberInviteBinding
import javax.inject.Inject


class InviteMemberFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: InviteViewModel
    private lateinit var binding: FragmentMemberInviteBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_member_invite, container, false)
        setupViews()
        return binding.root
    }

    private fun setupViews() {
        // PAGE TITLE
        requireActivity().title = viewModel.pageTitle
        // MESSAGE
        viewModel.message.observe(viewLifecycleOwner, Observer {
            if (!it.isNullOrBlank()) {
                Snackbar.make(binding.coordinator, it, Snackbar.LENGTH_SHORT).show()
                viewModel.messageHandled()
            }
        })
        // TITLE
        viewModel.email.observe(viewLifecycleOwner, Observer {
            if (!it.isNullOrEmpty() && it != binding.email.text.toString()) {
                binding.email.setText(it)
            }
        })
        binding.email.doOnTextChanged { text, start, count, after ->
            binding.email.error = null
            viewModel.setEmail(text.toString())
        }
        viewModel.isEmailValid.observe(viewLifecycleOwner, Observer {
            binding.email.error = it
        })
        // INVITE
        binding.invite.setOnClickListener {
            ViewUtil.hideKeyboard(requireActivity())
            viewModel.invite()
        }
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity as MemberComponentProvider).memberComponent.inject(this)
        viewModel = ViewModelProvider(this, viewModelFactory).get(InviteViewModel::class.java)
    }
}