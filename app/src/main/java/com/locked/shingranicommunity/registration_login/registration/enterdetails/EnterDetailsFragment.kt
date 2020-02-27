
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
import com.locked.shingranicommunity.databinding.FragmentEnterDetailsBinding
import com.locked.shingranicommunity.registration_login.registration.RegistrationActivity
import com.locked.shingranicommunity.registration_login.registration.RegistrationViewModel
import com.locked.shingranicommunity.registration_login.registration.login.LoginActivity
import javax.inject.Inject

class EnterDetailsFragment : Fragment() {

    @Inject
    lateinit var registrationViewModel: RegistrationViewModel
    @Inject
    lateinit var enterDetailsViewModel: EnterDetailsViewModel
    lateinit var mBinding: FragmentEnterDetailsBinding

    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?,savedInstanceState: Bundle?): View? {
        mBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_enter_details, container, false)

        enterDetailsViewModel = EnterDetailsViewModel()
        enterDetailsViewModel.enterDetailsState.observe(this,
            Observer<EnterDetailsViewState> { state ->
                when (state) {
                    is EnterDetailsSuccess -> {
                        registrationViewModel.updateUserData( mBinding.registerUsername.text.toString(), mBinding.registerPassword.text.toString(),mBinding.registerName.text.toString())
                        registrationViewModel.registerUser()

                        (activity as RegistrationActivity).onTermsAndConditionsAccepted()
                    }
                    is EnterDetailsError -> {
                        mBinding.txtError.text = state.error
                        mBinding.txtError.visibility = View.VISIBLE
                    }
                }
            })

        setupViews()
        return mBinding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity!! as RegistrationActivity).registerComponent.inject(this)
    }

    private fun setupViews() {

        mBinding.registerUsername.doOnTextChanged { _, _, _, _ -> mBinding.txtError.visibility = View.INVISIBLE }

        mBinding.registerPassword.doOnTextChanged { _, _, _, _ -> mBinding.txtError.visibility = View.INVISIBLE }
        mBinding.registerPasswordConform.doOnTextChanged { _, _, _, _ -> mBinding.txtError.visibility = View.INVISIBLE }

       mBinding.btnRegister.setOnClickListener {
            enterDetailsViewModel.validateInput(mBinding.registerUsername.text.toString(), mBinding.registerPassword.text.toString(), mBinding.registerPasswordConform.text.toString())

        }
        mBinding.btnJoiningPermission.setOnClickListener {
            var intent = Intent(activity, LoginActivity::class.java)
            intent.putExtra("message",registrationViewModel.message)
            startActivity(intent)
        }
    }
}

sealed class EnterDetailsViewState
object EnterDetailsSuccess : EnterDetailsViewState()
data class EnterDetailsError(val error: String) : EnterDetailsViewState()
