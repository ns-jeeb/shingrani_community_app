package com.locked.shingranicommunity.dashboard.event.details

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.locked.shingranicommunity.R
import com.locked.shingranicommunity.databinding.EventDetailsFragmentBinding
import com.locked.shingranicommunity.di2.viewmodel.ViewModelProviderFactory
import com.locked.shingranicommunity.models.Item
import javax.inject.Inject

class DetailsFragment : Fragment() {

    companion object {
        fun newInstance(item: Item) :DetailsFragment {
            var fragment = DetailsFragment()
            var bundle: Bundle = Bundle()
//            bundle.putParcelable("extra_item",item)
            fragment.arguments = bundle
            return fragment
        }
    }
    @Inject
    lateinit var viewModel: DetailsViewModel
    lateinit var mBinding: EventDetailsFragmentBinding
    @Inject
    lateinit var viewModelProvider: ViewModelProviderFactory


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.event_details_fragment, container, false)
//        var item = arguments?.getParcelable<Item>("extra_item")
        viewModel = ViewModelProviders.of(this,viewModelProvider).get(DetailsViewModel::class.java)
//        if (!item?.fields?.get(5)?.value.isNullOrBlank()){
//            mBinding.attendees.text = "${item?.fields?.get(5)?.value?.split(",")?.size} : Attend/s"
//        }
//        if (!item?.fields?.get(6)?.value.isNullOrBlank()){
//            mBinding.notAttendees.text = "${item?.fields?.get(6)?.value?.split(",")?.size} : Not Attend"
//        }

        return mBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(DetailsViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity as DetailsActivity).dashboardComponent.inject(this)
    }

}
