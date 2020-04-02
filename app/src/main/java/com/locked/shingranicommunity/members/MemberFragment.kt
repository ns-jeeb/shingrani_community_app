package com.locked.shingranicommunity.members

import android.content.Context
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import com.locked.shingranicommunity.R
import com.locked.shingranicommunity.ViewModelProviderFactory
import com.locked.shingranicommunity.databinding.MemberFragmentBinding
import javax.inject.Inject

class MemberFragment : DialogFragment(), View.OnClickListener {

    companion object {
        fun newInstance() = MemberFragment()
    }

    private lateinit var viewModel: FragmentMemberViewModel
    private lateinit var mBinding: MemberFragmentBinding
    @Inject
    lateinit var viewModelProvider: ViewModelProviderFactory

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.member_fragment, container, false)
        mBinding.btnSubmit.setOnClickListener(this)
        viewModel.nameResult.observe(this@MemberFragment, Observer {
            val loginResult = it ?: return@Observer
            if (loginResult.error != null) {
                mBinding.edMemberName.error = getString(R.string.error_invalid_email)
            }
            if (loginResult.success != null) {
                mBinding.edMemberName.error = getString(R.string.error_invalid_email)
            }
        })
        viewModel.emailFormState.observe(this@MemberFragment, Observer {
            val emailResult = it ?: return@Observer
            if (emailResult.emailError != null) {
                mBinding.btnSubmit.isEnabled =false
                mBinding.edMemberEmail.error = getString(R.string.error_invalid_email)
            }else{
                mBinding.btnSubmit.isEnabled = true
            }
        })

        mBinding.edMemberEmail.apply {
            doAfterTextChanged {
                viewModel.emailDataChanged(
                    mBinding.edMemberEmail.text.toString(),
                    mBinding.edMemberName.text.toString()
                )
            }

            mBinding.btnSubmit.setOnClickListener {
                mBinding.btnSubmit.visibility = View.VISIBLE
                viewModel.inviteMember(
                    mBinding.edMemberEmail.text.toString(),
                    mBinding.edMemberName.text.toString()

                ).observe(this@MemberFragment, Observer {
                    if (it != null) {
                        mBinding.txtPageTitle.text = it
                    }

                })
            }
        }
        return mBinding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity as MemberActivity).memberComponent.inject(this)
        viewModel =
            ViewModelProviders.of(this, viewModelProvider).get(FragmentMemberViewModel::class.java)
    }

    override fun onClick(v: View?) {
        if (v == mBinding.btnSubmit) {

        }
    }
}


