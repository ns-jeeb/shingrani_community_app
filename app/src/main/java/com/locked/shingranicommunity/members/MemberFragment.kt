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
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.locked.shingranicommunity.R
import com.locked.shingranicommunity.ViewModelProviderFactory
import com.locked.shingranicommunity.databinding.MemberFragmentBinding
import javax.inject.Inject

class MemberFragment : Fragment(),View.OnClickListener {

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
//        viewModel.nameResult.observe(this@MemberFragment, Observer {
//            val loginResult = it ?: return@Observer
//            if (loginResult.hasError()) {
//                mBinding.edMemberName.error = getString(R.string.error_invalid_email)
//            }
////            if (loginResult.success != null) {
////                mBinding.edMemberName.error = getString(R.string.error_invalid_email)
////            }
//        })
        viewModel = ViewModelProviders.of(this, viewModelProvider).get(FragmentMemberViewModel::class.java)
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
        }
        return mBinding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity as MemberActivity).memberComponent.inject(this)
    }

    override fun onClick(v: View?) {
        if (v == mBinding.btnSubmit) {
            mBinding.btnSubmit.visibility = View.VISIBLE
            viewModel.inviteMember(mBinding.edMemberEmail.text.toString(),mBinding.edMemberName.text.toString()).observe(this@MemberFragment, Observer {
                if (!it.message.isBlank()){
                    mBinding.txtPageTitle.text = it.email
                    mBinding.edMemberEmail.setText("")
                    mBinding.edMemberName.setText("")
                    (activity as MemberActivity).closeMemberFragment(true)
                }
                else{
                    mBinding.txtPageTitle.text = it?.errorType
                }
            })
        }
    }
}


