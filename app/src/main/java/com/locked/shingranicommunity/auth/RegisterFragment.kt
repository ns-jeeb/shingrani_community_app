package com.locked.shingranicommunity.auth

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.widget.doAfterTextChanged
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
        binding.registerEmail.doOnTextChanged { text, _, _, _ ->viewModel.setEmail(text.toString())}
        binding.registerName.doOnTextChanged { text, _, _, _ ->viewModel.setName(text.toString())}
        binding.registerPassword.doOnTextChanged { text, _, _, _ ->viewModel.setPassword(text.toString())}
        binding.registerPasswordConform.doOnTextChanged {text, _, _, _ ->viewModel.setPasswordConfirm(text.toString())}
        binding.btnLogin.setOnClickListener { viewModel.onLoginPress() }
        binding.btnRegister.setOnClickListener { viewModel.onRegisterPress() }
        viewModel.message.observe(viewLifecycleOwner, Observer {
            it?.let { Snackbar.make(binding.coordinator, it, Snackbar.LENGTH_SHORT).show() }
            viewModel.messageHandled()
        })
        viewModel.loading.observe(viewLifecycleOwner, Observer { loading ->
            enableControls(!loading)
        })
    }

    private fun enableControls(enable: Boolean) {
        binding.registerEmail.isEnabled = enable
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
