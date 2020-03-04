package com.locked.shingranicommunity.members

import android.content.Context
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil

import com.locked.shingranicommunity.R
import com.locked.shingranicommunity.ViewModelProviderFactory
import com.locked.shingranicommunity.databinding.MemberFragmentBinding
import javax.annotation.Nullable
import javax.inject.Inject

class MemberFragment : Fragment(),View.OnClickListener {

    companion object {
        fun newInstance() = MemberFragment()
    }

    private lateinit var viewModel: FragmentMemberViewModel
    private lateinit var mBinding : MemberFragmentBinding
    @Inject
    lateinit var viewModelProvider: ViewModelProviderFactory

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = DataBindingUtil.inflate(inflater,R.layout.member_fragment, container, false)
        var email = mBinding.edMemberEmail.text.toString()
        var name = mBinding.edMemberName.text.toString()
        viewModel.inviteMember(email,name)

        mBinding.btnSubmit.setOnClickListener(this)
        return mBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity as MemberActivity).memberComponent.inject(this)
        viewModel = ViewModelProviders.of(this,viewModelProvider).get(FragmentMemberViewModel::class.java)
    }

    override fun onClick(v: View?) {
        if (v == mBinding.btnSubmit) {

        }
    }

}
