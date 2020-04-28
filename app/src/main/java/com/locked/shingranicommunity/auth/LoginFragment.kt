package com.locked.shingranicommunity.auth

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.locked.shingranicommunity.R
import com.locked.shingranicommunity.databinding.ActivityLoginBinding
import javax.inject.Inject

class LoginFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    lateinit var viewModel: LoginViewModel
    lateinit var binding: ActivityLoginBinding

    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?,savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.activity_login, container, false)
        setupViews()
        return binding.root
    }

    private fun setupViews() {
        binding.loginEmail.doOnTextChanged { text, start, count, after -> viewModel.setEmail(text.toString()) }
        binding.loginPassword.doOnTextChanged { text, start, count, after -> viewModel.setPassword(text.toString()) }
        binding.btnLogin.setOnClickListener { viewModel.onLoginPress() }
        binding.btnInvited.setOnClickListener { viewModel.onRegisterPress() }

        viewModel.message.observe(viewLifecycleOwner, Observer {
            it?.let {
                Snackbar.make(binding.coordinator, it, Snackbar.LENGTH_SHORT).show()
                viewModel.messageHandled()
            }
        })
        viewModel.loading.observe(viewLifecycleOwner, Observer { loading ->
            enableControls(!loading)
        })
        viewModel.email.observe(viewLifecycleOwner, Observer {
            if (!it.isNullOrEmpty() && !it.equals(binding.loginEmail.text.toString())) {
                binding.loginEmail.setText(it)
            }
        })
    }

    private fun enableControls(enable: Boolean) {
        binding.loginEmail.isEnabled = enable
        binding.loginPassword.isEnabled = enable
        binding.btnLogin.isEnabled = enable
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity as AuthComponentProvider).authComponent.inject(this)
        viewModel = ViewModelProvider(this, viewModelFactory).get(LoginViewModel::class.java)
    }
}