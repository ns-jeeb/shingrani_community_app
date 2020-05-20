package com.locked.shingranicommunity.registration_login.registration.enterdetails

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.locked.shingranicommunity.R
import com.locked.shingranicommunity.databinding.FragmentRegisterBinding
import com.locked.shingranicommunity.registration_login.registration.RegistrationActivity
import com.locked.shingranicommunity.registration_login.registration.RegistrationViewModel
import com.locked.shingranicommunity.registration_login.registration.login.LoginActivity
import javax.inject.Inject

class RegisterFragment : Fragment() {

    @Inject
    lateinit var registrationViewModel: RegistrationViewModel
    @Inject
    lateinit var enterDetailsViewModel: EnterDetailsViewModel

    lateinit var binding: FragmentRegisterBinding

    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?,savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_register, container, false)
        enterDetailsViewModel.enterDetailsState.observe(this,
            Observer<EnterDetailsViewState> { state ->
                when (state) {
                    is EnterDetailsSuccess -> {
                        registrationViewModel.updateUserData( binding.registerEmail.text.toString(), binding.registerPassword.text.toString(),binding.registerName.text.toString())
                        registrationViewModel.registerUser()
                        enterDetailsViewModel.registerUser(binding.registerEmail.text.toString(),binding.registerPassword.text.toString(),binding.registerName.text.toString()).observe(this,
                            Observer {
                                if (it.isDataValid){
                                    var intent = Intent(activity, LoginActivity::class.java)
                                    startActivity(intent)
                                    activity?.finish()
                                }
                                binding.txtError.text = it.message
                            })
                    }
                    is EnterDetailsError -> {
                        binding.txtError.text = state.error
                        binding.txtError.visibility = View.VISIBLE
                    }
                }
            })

        setupViews()
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity!! as RegistrationActivity).registerComponent.inject(this)
    }

    private fun setupViews() {

        binding.registerEmail.doOnTextChanged { _, _, _, _ -> binding.txtError.visibility = View.INVISIBLE }

        binding.registerPassword.doOnTextChanged { _, _, _, _ -> binding.txtError.visibility = View.INVISIBLE }
        binding.registerPasswordConform.doOnTextChanged { _, _, _, _ -> binding.txtError.visibility = View.INVISIBLE }

       binding.btnRegister.setOnClickListener {
            enterDetailsViewModel.validateInput(binding.registerEmail.text.toString(),
                binding.registerPassword.text.toString(),
                binding.registerPasswordConform.text.toString(),
                binding.registerName.text.toString()
            )

        }
        binding.btnLogin.setOnClickListener {
            var intent = Intent(activity, LoginActivity::class.java)
            intent.putExtra("message","registrationViewModel.message")
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }
}

sealed class EnterDetailsViewState
object EnterDetailsSuccess : EnterDetailsViewState()
data class EnterDetailsError(val error: String) : EnterDetailsViewState()
