package com.locked.shingranicommunity.dashboard.event.create_event.ui.createevent

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.locked.shingranicommunity.R
import com.locked.shingranicommunity.ViewModelProviderFactory
import com.locked.shingranicommunity.dashboard.event.create_event.CreateItemActivity
import com.locked.shingranicommunity.databinding.CreateEventFragmentBinding
import javax.inject.Inject

class CreateEventFragment : Fragment() ,View.OnClickListener{
    override fun onClick(v: View?) {
        var type = mBinding.crEventType.getItemAtPosition(1).toString()
        if (v?.id == R.id.btn_cr_event){
            viewModel.createEvent(
                mBinding.crEventName.text.toString(),
                type,
                mBinding.crEventAddress.text.toString(),
                mBinding.crEventDateTime.text.toString(),
                mBinding.crEventDetails.text.toString()
            )
        }
    }

    lateinit var mBinding: CreateEventFragmentBinding

    companion object {
        fun newInstance() = CreateEventFragment()
    }

    @Inject
    lateinit var viewModelProviders: ViewModelProviderFactory
    lateinit var viewModel: CreateEventViewModel

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View {
//        viewModel.lifecycleOwner = this
        mBinding = DataBindingUtil.inflate(inflater,R.layout.create_event_fragment,container,false)

        mBinding.btnCrEvent.setOnClickListener(this)
        return mBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity as CreateItemActivity).dashboardCompunent.inject(this)
    }

}
