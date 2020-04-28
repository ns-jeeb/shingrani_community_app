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
import com.locked.shingranicommunity.databinding.FragmentRegisterBinding
import javax.inject.Inject

class RegisterFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    lateinit var viewModel: RegisterViewModel
    lateinit var binding: FragmentRegisterBinding

    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?,savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_register, container, false)
        setupViews()
        return binding.root
    }

    private fun setupViews() {
        binding.registerUsername.doOnTextChanged { text, start, count, after -> viewModel.setEmail(text.toString()) }
        binding.registerName.doOnTextChanged { text, start, count, after -> viewModel.setName(text.toString()) }
        binding.registerPassword.doOnTextChanged { text, start, count, after -> viewModel.setPassword(text.toString()) }
        binding.registerPasswordConform.doOnTextChanged { text, start, count, after -> viewModel.setPasswordConfirm(text.toString()) }
        binding.btnRegister.setOnClickListener { viewModel.onRegisterPress() }
        binding.btnLogin.setOnClickListener { viewModel.onLoginPress() }
        viewModel.message.observe(viewLifecycleOwner, Observer {
            it?.let { Snackbar.make(binding.coordinator, it, Snackbar.LENGTH_SHORT).show() }
        })
        viewModel.loading.observe(viewLifecycleOwner, Observer { loading ->
            enableControls(!loading)
        })
    }

    private fun enableControls(enable: Boolean) {
        binding.registerUsername.isEnabled = enable
        binding.registerName.isEnabled = enable
        binding.registerPassword.isEnabled = enable
        binding.registerPasswordConform.isEnabled = enable
        binding.btnRegister.isEnabled = enable
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity as AuthComponentProvider).authComponent.inject(this)
        viewModel = ViewModelProvider(this, viewModelFactory).get(RegisterViewModel::class.java)
    }
}
